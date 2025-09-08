export async function loadSolution(numbers, showErrors) {
    const signal = AbortSignal.timeout(2500);
    await fetch('/api/solve-24', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({ numbers }),
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
    const math = document.createElement('math');
    math.style.display = 'block';
    const row = document.createElement('mrow');
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
    const open = document.createElement('mo');
    open.textContent = '(';
    items.push(open);

    convertOperand(expression.operation.lhs, items);

    const op = document.createElement('mo');
    op.textContent = replaceOperator(expression.operation.operator);
    items.push(op);

    convertOperand(expression.operation.rhs, items);

    const close = document.createElement('mo');
    close.textContent = ')';
    items.push(close)
    return items;
}

function convertOperand(operand, items) {
    switch (operand.type) {
        case 'number':
            const mn = document.createElement('mn');
            mn.textContent = operand.value;
            items.push(mn);
            break;
        case 'expression':
            convertExpression(operand, items);
            break;
    }

}

function replaceOperator(operator) {
    switch (operator) {
        case '+':
            return '+';
        case '*':
            return '×'
        case '/':
            return '÷'
        case '-':
            return '-';
    }
}

function showSingleItem(show) {
    for (const id of ["solutions", "error", "no-result"]) {
        const element = document.getElementById(id);
        if (element) {
            element.style.display = (show === id ) ? "block" : "none";
        }
    }
}