function validateNameSurname(name) {
    return /^[a-zA-Z\s]+$/.test(name);
}
function validatePhone(phone) {
    return /^[0-9]{10}$/.test(phone);
}
function validateEmail(email) {
    return /^[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/.test(email);
}
function validatePassword(password) {
    return /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/.test(password);
}
