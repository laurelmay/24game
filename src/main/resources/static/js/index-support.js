import {loadSolution} from "./load-solution.js";
import {
    applyCreateGameSettings,
    applyRulesToInputs,
    loadCreateGameSettings, loadGameRules,
    saveCreateGameSettings,
    saveGameRules
} from "./game-rules.js";
import {showSingleItem} from "./util.js";

let isSolved = false;
let requestCancellation = {
    abortController: undefined,
    timeoutHandle: undefined,
};

class MultipleRequestError extends Error {
    constructor(message) {
        super(message);
    }
}

export async function loadGame() {
    isSolved = false;
    const rules = loadGameRules();
    const solveConfig = loadCreateGameSettings();
    const signal = getCancellationSignal();
    document.getElementById("solutions").style.display = "none";
    await fetch(`/api/random-game`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            ...solveConfig,
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
        .then(response => response.json())
        .then((game) => showGameDetails(game))
        .catch(async (error) => {
            console.error(error);
            if (!(error instanceof MultipleRequestError)) {
                try {
                    const errData = await error.data.json();
                    document.getElementById("error-details").textContent = errData.message;
                } catch {
                    document.getElementById("error-details").textContent = error.message;
                }
                showSingleItem("error");
            }
        });
}

function showGameDetails(game) {
    const gameContainer = document.getElementById("game-container");

    const math = document.createElement("math");
    const numbers = game.numbers;
    for (let i = 0; i < numbers.length; i++) {
        const number = numbers[i];
        createNumberElement(number, math)
    }
    gameContainer.replaceChildren(math);

    document.getElementById("error").style.display = "none";
    document.getElementById("solve-button").onclick = () => {
        if (isSolved) {
            return;
        }
        loadSolution(numbers, true, getCancellationSignal()).then(() => {
            isSolved = true;
        }).catch(() => {
            isSolved = false;
        });
    };
}

function createNumberElement(value, parent) {
    const numberElement = document.createElement("mn");
    numberElement.innerText = value;
    parent.appendChild(numberElement);
}

function getCancellationSignal() {
    if (requestCancellation.abortController) {
        requestCancellation.abortController.abort(new MultipleRequestError('Additional request detected, cancelling previous request'));
        clearTimeout(requestCancellation.timeoutHandle);
    }
    const abortController = new AbortController();
    requestCancellation.timeoutHandle = setTimeout(() => abortController.abort(new Error('Timeout exceeded')), 2500);
    requestCancellation = { abortController, timeoutHandle: requestCancellation.timeoutHandle };
    return abortController.signal;
}

window.onload = async () => {
    await applyRulesToInputs();
    await applyCreateGameSettings();
    document.getElementById("error").style.display = "none";
    await loadGame();
};

document.getElementById("create-button").addEventListener('click', async () => {
    saveCreateGameSettings();
    saveGameRules();
    await loadGame();
});