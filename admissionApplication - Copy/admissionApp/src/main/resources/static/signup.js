document.addEventListener('DOMContentLoaded', function () {
    const signupForm = document.querySelector('section'); // Corrected query selector
    signupForm.style.opacity = 0;

    setTimeout(() => {
        signupForm.style.transition = 'opacity 1s ease-in-out'; // Corrected 'transition'
        signupForm.style.opacity = 1;
    }, 500);
});
