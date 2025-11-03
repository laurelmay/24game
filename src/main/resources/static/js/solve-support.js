import {loadSolution} from "./load-solution.js";
import {applyRulesToInputs, saveGameRules} from "./game-rules.js";

export async function run() {
    const a = parseInt(document.getElementById("first").value);
    const b = parseInt(document.getElementById("second").value);
    const c = parseInt(document.getElementById("third").value);
    const d = parseInt(document.getElementById("fourth").value);

    await loadSolution([a, b, c, d], true);
}

window.onload = () => {
    applyRulesToInputs();
    for (const element of document.getElementsByClassName('number-input')) {
        element.onfocus = () => {
            if (element.value) {
                element.select();
            }
        }
    }
}
document.getElementById("form").onsubmit = async (event) => {
    saveGameRules();
    event.preventDefault();
    await run();
}