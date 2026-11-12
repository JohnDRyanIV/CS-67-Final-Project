const gameBoard = document.getElementById("board");
const overDiv = document.getElementById("game-over");

let turn = -1;
resetGame();

function renderBoard(rows, cols) {
  gameBoard.innerHTML = "";
  for (let row = 0; row < rows; row++) {
    for (let col = 0; col < cols; col++) {
      const div = document.createElement("div");
      div.className = "cell";
      div.onclick = () => dropPiece(col);
      div.onmouseenter = () => hoverPiece(col);
      gameBoard.appendChild(div);
    }
  }
}

function hoverPiece(col) {
  let unplaced = document.querySelector("[data-placed='false']");
  let cell = gameBoard.children[col];
  let piece = document.createElement("div");

  if (unplaced) {
    unplaced.parentElement.removeChild(unplaced);
  }

  if (cell.children.length || overDiv.dataset.player !== "null") {
    return;
  }

  piece.classList.add("piece");
  piece.dataset.player = turn;
  piece.dataset.placed = false;
  cell.appendChild(piece);
}

async function dropPiece(col) {
  // Needs to get board state first
  // Also needs to send post request
}

async function makeMove(row, col) {
  if (row == -1) return alert("Column full");
  // turn = api.getTurn()
  let cell = gameBoard.children[row * 7 + col];
  let piece = document.createElement("div");
  piece.classList.add("piece");
  piece.dataset.player = -turn;
  piece.dataset.placed = true;
  cell.appendChild(piece);

  let unplaced = document.querySelector("[data-placed='false']");
  let unplacedY = unplaced.getBoundingClientRect().y;
  let placedY = piece.getBoundingClientRect().y;
  let yDiff = unplacedY - placedY;
  piece.animate(
    [
      { transform: `translateY(${yDiff}px)`, offset: 0 },
      { transform: `translateY(0px)`, offset: 0.6 },
      { transform: `translateY(${yDiff / 20}px)`, offset: 0.8 },
      { transform: `translateY(0px)`, offset: 0.95 },
    ],
    {
      duration: 450,
      easing: "ease-in-out",
      iterations: 1,
    },
  );
  hoverPiece(col);
}

async function resetGame() {
  renderBoard(6, 7);
  overDiv.dataset.player = null;
  turn = -1;
}
