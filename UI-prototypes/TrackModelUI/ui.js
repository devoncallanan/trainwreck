
function incrementG() {

	if(document.getElementById((greenLoc+10)%100).getAttribute('class') != 'green') {
		var temp1 = document.getElementById(greenLoc);
		temp1.removeAttribute("class");
		greenLoc = (greenLoc+10)%100;
		var temp2 = document.getElementById(greenLoc);
		temp2.setAttribute('class', 'green');
	}

}
function incrementR() {
	if (document.getElementById((redLoc+10)%100).getAttribute('class') != 'red') {
		var temp1 = document.getElementById(redLoc);
		temp1.removeAttribute("class");
		redLoc = (redLoc+10)%100;
		var temp2 = document.getElementById(redLoc);
		temp2.setAttribute('class', 'red');
	}
}
function resetTest() {
	greenLoc = 0;
	redLoc  = 1;
}
function getBreak() {
	var temp1 = document.getElementById('break');
	var temp2 = temp1.value.split(" ");
	if (temp2[0] == 'green') {
		var temp3 = temp2[1]*10;
		document.getElementById(temp3).setAttribute('class','green');
	}
	else {
		var temp3 = temp2[1]*10 +1;
		document.getElementById(temp3).setAttribute('class','red');		
	}
}
function getFix() {
	var temp1 = document.getElementById('fix');
	var temp2 = temp1.value.split(" ");
	if (temp2[0] == 'green') {
		var temp3 = temp2[1]*10;
		document.getElementById(temp3).setAttribute('class','empty');
	}
	else {
		var temp3 = temp2[1]*10 +1;
		document.getElementById(temp3).setAttribute('class','empty');		
	}	
}




var greenLoc = 0;
var redLoc = 1;

var infoTable = document.getElementById('infotable');

for (var i = 0; i< 10; i++) {
	var row = document.createElement('tr');
	for (var j = 0; j < 2; j++) {
		var cell = document.createElement('td');
		cell.setAttribute('id', (i*10 + j));
		cell.innerHTML = 'Block ' + i;
		row.appendChild(cell);
	}
	infoTable.appendChild(row);
	
}

var greenInc = window.setInterval(incrementG, 1300);
var redInc = window.setInterval(incrementR, 1500);