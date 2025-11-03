export function createMathmlElement(elementName) {
    return document.createElementNS("http://www.w3.org/1998/Math/MathML", elementName);
}