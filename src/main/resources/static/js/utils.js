function checkAnswer(selectedWord) {

    const correctWord = document.getElementById("correctWord").innerHTML;
    const resultDiv = document.getElementById("result");
    const nextQuestionDiv = document.getElementById("nextQuestion");

    const choices = document.querySelectorAll("#choices li a");

    // Disable all choices
    choices.forEach(choice => choice.classList.add("disabled"));

    if (selectedWord === correctWord) {
        resultDiv.innerHTML = "Correct choice!";
    } else {
        resultDiv.innerHTML = "Sorry, your choice was incorrect.";
    }

    resultDiv.style.display = "block";
    nextQuestionDiv.innerHTML = "Next Question";

}


function skipQuestion() {
    // Optional: You can add any additional logic here if needed
    window.location.href = "/start";
}