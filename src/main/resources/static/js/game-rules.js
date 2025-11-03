function defaultGameRules() {
    return {
        targetValue: 24,
        allowedOperations: [
            "ADDITION",
            "SUBTRACTION",
            "MULTIPLICATION",
            "DIVISION",
        ],
    };
}


export function readRulesFromInputs() {
    const operations = [];
    for (const operation of ["ADDITION", "SUBTRACTION", "MULTIPLICATION", "DIVISION", "EXPONENTIATION", "LOGARITHM", "ROOT"]) {
        const element = document.querySelector(`input[value="${operation}"][type="checkbox"]`);
        if (element.checked) {
            operations.push(element.value);
        }
    }
    const targetValue = readNumber(() => document.getElementById("target-value").value, 24);
    return {
        targetValue,
        allowedOperations: operations,
    };
}

export function saveGameRules() {
    const rules = readRulesFromInputs();
    localStorage.setItem("gameRules", JSON.stringify(rules));
    return rules;
}

export function applyRulesToInputs() {
    const rules = loadGameRules();
    for (const element of document.querySelectorAll(`.rule-checkbox > input[type="checkbox"]`)) {
        console.log(element.value);
        element.checked = rules.allowedOperations.includes(element.value);
    }
    document.getElementById("target-value").value = rules.targetValue;
}

export function loadGameRules() {
    try {
        const rules = localStorage.getItem("gameRules");
        return rules ? JSON.parse(rules) : defaultGameRules();
    } catch (e) {
        return defaultGameRules();
    }
}

function readNumber(reader, defaultValue) {
    try {
        return parseInt(reader(), 10) || defaultValue;
    } catch (e) {
        return defaultValue;
    }
}

function defaultCreateGameSettings() {
    return {
        solvability: 'SOLVABLE',
        minimumNumber: 0,
        maximumNumber: 13,
    };
}

export function readCreateGameSettings() {
    const requireSolvable = document.getElementById("require-solvable").checked ? 'SOLVABLE' : 'UNKNOWN';
    const minimumNumber = readNumber(() => document.getElementById("minimum-value").value, 0);
    const maximumNumber = readNumber(() => document.getElementById("maximum-value").value, 13);
    return {
        solvability: requireSolvable,
        minimumNumber,
        maximumNumber,
    };
}

export function saveCreateGameSettings() {
    const settings = readCreateGameSettings();
    localStorage.setItem("createGameSettings", JSON.stringify(settings));
    return settings;
}

export function loadCreateGameSettings() {
    try {
        const readValue = localStorage.getItem("createGameSettings");
        return readValue ? JSON.parse(readValue) : defaultCreateGameSettings();
    } catch (e) {
        return defaultGameRules();
    }
}

export function applyCreateGameSettings() {
    const settings = loadCreateGameSettings();
    document.getElementById("require-solvable").checked = settings.solvability === 'SOLVABLE';
    document.getElementById("minimum-value").value = settings.minimumNumber;
    document.getElementById("maximum-value").value = settings.maximumNumber;
}