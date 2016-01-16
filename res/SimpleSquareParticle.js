// Simple class example

function SimpleSquareParticle(posX, posY, nbr) {
		this.x = posX;
		this.y = posY;
		this.velX = 0;
		this.velY = 0;
		this.accelX = 0;
		this.accelY = 0;
		this.color = "#FF0000";
		this.radiuX = 5;
		this.radiuY = 5;
		this.number =  nbr;
		this.server = "Not assigned";
		this.dishes = 1;
}

//The function below returns a Boolean value representing whether the point with the coordinates supplied "hits" the particle.
SimpleSquareParticle.prototype.hitTest = function(hitX,hitY) {
	return((hitX > this.x - this.radiusX)&&(hitX < this.x + this.radiusX)&&(hitY > this.y - this.radiusY)&&(hitY < this.y + this.radiusY));
}

//A function for drawing the particle.
SimpleSquareParticle.prototype.drawToContext = function(theContext) {
	theContext.fillStyle = "red";
	theContext.fillStyle = this.color;
	theContext.fillRect(this.x - this.radiusX, this.y - this.radiusY, 2*this.radiusY, 2*this.radiusX);
	//theContext.fillStyle = #FF0000;


	//theContext.fillText(this.x - this.radius, this.y - this.radius, this.number, 10, 50);
}

SimpleSquareParticle.prototype.removeFromContext = function(theContext) {
	theContext.fillStyle = "white";
	theContext.fillRect(this.x - this.radius, this.y - this.radius, 2*this.radius, 2*this.radius);
	//theContext.fillStyle = #FF0000;


	//theContext.fillText(this.x - this.radius, this.y - this.radius, this.number, 10, 50);
}