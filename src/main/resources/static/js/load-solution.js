import {loadGameRules, saveGameRules} from "./game-rules.js";
import {createMathmlElement} from "./mathml-support.js";

export async function loadSolution(numbers, showErrors) {
    const rules = loadGameRules();
    const signal = AbortSignal.timeout(2500);
    await fetch('/api/solve-24', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({
            numbers,
            gameRuleSettings: rules,
        }),
        signal,
    })
        .then(res => {
            if (res.ok) {
                return res;
            }
            const e = new Error(res.statusText);
            e.data = res;
            throw e;
        })
        .then(res => res.json())
        .then(data => createSolutionList(data.solutions))
        .catch(async (err) => {
            if (!showErrors) {
                console.log(err)
            }
            try {
                const errData = await err.data.json();
                document.getElementById("error-details").textContent = errData.message;
            } catch {
                document.getElementById("error-details").textContent = err.message;
            }
            document.getElementById("solutions").style.display = "none";
            console.log(err)
        });
}

function createSolutionList(solutions) {
    const parent = document.getElementById('solutions');
    if (!solutions || solutions.length < 1) {
        showSingleItem("no-result");
        return;
    }
    const count = document.createElement('p');

    const solutionsList = document.createElement('div');
    solutionsList.id = 'solutions-list';
    solutionsList.style.display = 'none';
    solutionsList.replaceChildren(...solutions.map(createListItem));

    const showSolutions = document.createElement('button');
    showSolutions.className = 'btn btn-primary btn-small';
    showSolutions.id = 'show-solutions';
    showSolutions.textContent = 'Show Solutions';

    const hideSolutions = document.createElement('button');
    hideSolutions.className = 'btn btn-primary btn-small';
    hideSolutions.id = 'hide-solutions';
    hideSolutions.textContent = 'Hide Solutions';
    hideSolutions.style.display = 'none';

    showSolutions.onclick = () => {
        solutionsList.style.display = 'inline-block';
        hideSolutions.style.display = 'inline-block';
        showSolutions.style.display = 'none';
    }
    hideSolutions.onclick = () => {
        solutionsList.style.display = 'none';
        showSolutions.style.display = 'inline-block';
        hideSolutions.style.display = 'none';
    }


    count.replaceChildren(
        document.createTextNode(`There ${solutions.length > 1 ? 'are' : 'is'} ${solutions.length} solution${solutions.length > 1 ? 's' : ''}`),
        showSolutions,
        hideSolutions,
    );

    parent.replaceChildren(count, solutionsList);
    showSingleItem("solutions");
}

function createListItem(solution) {
    const item = document.createElement('p');
    item.replaceChildren(convertToMathMl(solution));
    return item;
}

function convertToMathMl(solution) {
    const math = createMathmlElement('math');
    math.style.display = 'block';
    const row = createMathmlElement('mrow');
    math.replaceChildren(row);
    row.replaceChildren(...convertExpression({
        type: 'expression',
        operation: solution
    }, []));
    return math;
}

/**
 *
 * @param expression
 * @param items {Array} tst
 */
function convertExpression(expression, items) {
    switch (expression.operation.operator) {
        case '+':
        case '-':
        case '*':
            return convertInfixExpression(expression, items);
        case '/':
            return convertDivisionExpression(expression, items);
        case '^':
            return convertExponentiationExpression(expression, items);
        case '√':
            return convertRootExpression(expression, items);
        case 'log':
            return convertLogarithmExpression(expression, items);
        default:
            console.error(`Unknown operator: ${expression.operation.operator}`);
    }
    return items;
}

function convertOperand(operand, items) {
    switch (operand.type) {
        case 'number':
            const mn = createMathmlElement('mn');
            mn.textContent = operand.value;
            items.push(mn);
            break;
        case 'expression':
            convertExpression(operand, items);
            break;
    }
}

function convertInfixExpression(expression, items) {
    const open = createMathmlElement('mo');
    open.textContent = '(';
    items.push(open);

    convertOperand(expression.operation.lhs, items);

    const op = createMathmlElement('mo');
    op.textContent = replaceInfixOperator(expression.operation.operator);
    items.push(op);

    convertOperand(expression.operation.rhs, items);

    const close = createMathmlElement('mo');
    close.textContent = ')';
    items.push(close)
    return items;
}

function convertDivisionExpression(expression, items) {
    return simpleExpressionHelper('mfrac', true, expression, items);
}

function convertRootExpression(expression, items) {
    return simpleExpressionHelper('mroot', true, expression, items);
}

function convertExponentiationExpression(expression, items) {
    return simpleExpressionHelper('msup', true, expression, items);
}

function simpleExpressionHelper(outerElement, withParentheses, expression, items) {
    if (withParentheses) {
        const open = createMathmlElement('mo');
        open.textContent = '(';
        items.push(open);
    }

    const exponent = createMathmlElement(outerElement);

    const lhs = createMathmlElement('mrow');
    const lhsChildren = [];
    convertOperand(expression.operation.lhs, lhsChildren);
    lhs.replaceChildren(...lhsChildren);

    const rhs = createMathmlElement('mrow');
    const rhsChildren = [];
    convertOperand(expression.operation.rhs, rhsChildren);
    rhs.replaceChildren(...rhsChildren);

    exponent.replaceChildren(lhs, rhs);
    items.push(exponent);

    if (withParentheses) {
        const open = createMathmlElement('mo');
        open.textContent = ')';
        items.push(open);
    }

    return items;

}

function convertLogarithmExpression(expression, items) {
    const outer = createMathmlElement('mrow');
    const logarithm = createMathmlElement('msub');

    const log = createMathmlElement('mo');
    log.textContent = 'log';

    const base = createMathmlElement('mrow');
    const baseChildren = [];
    convertOperand(expression.operation.lhs, baseChildren);
    base.replaceChildren(...baseChildren);

    logarithm.replaceChildren(log, base);

    const antiLogarithm = createMathmlElement('mrow');
    const antiLogarithmChildren = [];
    convertOperand(expression.operation.rhs, antiLogarithmChildren);
    antiLogarithm.replaceChildren(...antiLogarithmChildren);

    outer.replaceChildren(logarithm, antiLogarithm);
    items.push(outer);
    return items;
}

function replaceInfixOperator(operator) {
    switch (operator) {
        case '+':
            return '+';
        case '*':
            return '×'
        case '-':
            return '-';
    }
}

function showSingleItem(show) {
    for (const id of ["solutions", "error", "no-result"]) {
        const element = document.getElementById(id);
        if (element) {
            element.style.display = (show === id) ? "block" : "none";
        }
    }
}