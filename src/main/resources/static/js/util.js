export function showSingleItem(show) {
    for (const id of ["solutions", "error", "no-result"]) {
        const element = document.getElementById(id);
        if (element) {
            element.style.display = (show === id) ? "block" : "none";
        }
    }
}