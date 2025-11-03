import {loadSolution} from "./load-solution.js";
import {
    applyCreateGameSettings,
    applyRulesToInputs,
    loadCreateGameSettings, loadGameRules,
    saveCreateGameSettings,
    saveGameRules
} from "./game-rules.js";

let isSolved = false;

export async function loadGame() {
    isSolved = false;
    const rules = loadGameRules();
    const solveConfig = loadCreateGameSettings();
    document.getElementById("solutions").style.display = "none";
    const signal = AbortSignal.timeout(2500);
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
        .then(response => response.json())
        .then((game) => showGameDetails(game))
        .catch((error) => {
            console.error(error);
            document.getElementById("error").style.display = "block";
            document.getElementById("error-details").innerText = error.message;
            document.getElementById("solutions").style.display = "none";
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
        loadSolution(numbers, true).then(() => {
            isSolved = true;
        });
    };
}

function createNumberElement(value, parent) {
    const numberElement = document.createElement("mn");
    numberElement.innerText = value;
    parent.appendChild(numberElement);
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