/*

1. connects -> send world request
  new world map is returned

2. initscreen called
  removed after 8 sec

3. clientside -> for all players online, loop and create container & ghost. append to the world

4. clientside -> after handling nudge & rotation, send the newest coordinate back to the server
  i.e.  each.getWorldPosition().x,
  i.e. 'sendBack_newPos'

5. serverside -> once server hears 'sendBack_newPos', updates the data on server,
  release back to all clients by emitting 'broadcast'

6. clientside -> loop the incoming dataset, and clientside dataset, updates accordingly

7. text is no longer working (some sort of deprecation)

8. wave 404 error

*/

const socket = io();
let world;
let myMap;

// let tileSize = 10;
let tileSizeWidth;
let tileSizeLength;

let worldSize = 144;
let sensor;

let containerMap;
let playerArrayClient = [];

let playerArrayServer;
let mapLoaded = false;

let currentPlayerNumber = 0;


let firstScreen;
let textHolder;



let ending = false;
let endScreen;

let mymin;
let mysec;

let treasure;
let treasureBox;

let canvasWidth = 150;
let canvasHeight = 150;
let mapTileWidth;
let mapTileHeight;
let mapColLength;
let mapRowLength;



// if (map[r][c] == 7) {
//   fill('blue')
//   rect(r * mapTileWidth, c * mapTileHeight, mapTileWidth, mapTileHeight);
// }
// if (map[r][c] == 0) {
//   fill('black')
//   rect(r * mapTileWidth, c * mapTileHeight, mapTileWidth, mapTileHeight);
// }
// return


function setup() {
	// noCanvas();
  createCanvas(canvasWidth, canvasHeight);


  // .id('sketchCanvas2');


  world = new World('VRScene');
  socket.emit('setupReady');

  socket.on('newMap', function(data) {
    // console.log('socket.on recieved new map from server', data.newMap);

    myMap = data.newMap;

    mapColLength = myMap.length
    mapRowLength;



    function findRowLength() {
      let rowLengthFound = false;

      for (let i = 0; i < mapColLength; i++) {
        if (rowLengthFound != true) {
          mapRowLength = myMap[i].length
          rowLengthFound = true;
        }
        return;
      }
      return;
    }
    findRowLength();

    tileSizeWidth = worldSize / mapColLength; //clean up a lil bit here
    tileSizeLength = worldSize / mapRowLength;
    console.log(mapRowLength,'mapRowLength');
    console.log(mapColLength,'mapColLength');

    mapTileWidth = canvasWidth / mapColLength;
    mapTileHeight = canvasHeight / mapRowLength;
    console.log(myMap);
    for (let r = 0; r < myMap.length; r++) {
      for (let c = 0; c < myMap[r].length; c++) {
        // stroke(2)


        if ( myMap[r][c] == 3 ) {
          fill('crimson')
        }
        else if ( myMap[r][c] == 1 ) {
          fill('green')
        }
        else if ( myMap[r][c] == 5 ) {
          fill('yellow')

        } else {
          fill('black')
        }

        rect(c * mapTileWidth, r * mapTileHeight, mapTileWidth, mapTileHeight);
      }
    }
  });







  avatar = new Box({
		red: 255, green: 255, blue: 0,
		width: 0.1, height: 0.1, depth: 0.1,
		z: -0.5, y: -0.5
	})
	// add the container to the user's HUD cursor
	world.camera.cursor.addChild(avatar);

  console.log('socket.emit worldReady')
  socket.emit('worldReady')

  currentPlayersFunc();
  broadcastFunc();

  //fog
	// world.threeSceneReference.fog = new THREE.FogExp2( 0xffffff, 0.1)
  world.camera.holder.setAttribute('wasd-controls','enabled:false');

	let ground = new Plane({x:0, y:0, z:0, width:worldSize, height:worldSize, rotationX:-90, metalness:0.25, asset:'asphalt'});
  ground.tag.object3D.userData.solid = true;
	world.add(ground);

} // end of setup




/*  from xx_testcamera  */
let avatar;
let rotationVector;
let rotY = 0;

function currentPlayersFunc() {

  socket.on('currentPlayers', function(data) {
    console.log('socket.on currentPlayers')
    playerArrayServer = data.currentPlayers;
    playerArrayServer.forEach((each) => {

      let dup = false;
      for (let i = 0; i < playerArrayClient.length; i++) {
        if (each.userId === playerArrayClient[i].id) {
          dup = true;
          // console.log("found a dup!");
          break;
        }
      }

      //make color consistent -> maybe texture on the wall

      if (!dup) {
          // console.dir(each);
          let character = new OBJ({
        		asset: 'ghost2_obj',
        		mtl: 'ghost2_mtl',
      		  // x:each.xPos, y:each.yPos, z:each.zPos,
            x:0, y:0, z:0,
        		rotationX:-60,
        		rotationY:180,
        		rotationZ:0,
        		scaleX:0.03,
        		scaleY:0.03,
        		scaleZ:0.04
        	});
          character.id = each.userId;

          let sensorBox = new Box({
            x: 0,
            y: 1,
            z: 0,
            opacity: 0.2,
            red: random(100,240), green:random(100,240), blue:random(100,240)
          });

          let container;

          if( each.userId === socket.id ) {
            console.log('player view');
            console.log(each.userId, socket.id);

            container = new Container3D({ x: 0, y: -2, z: 0});   //making container local variable solved it
            console.log('my' + each.xPos +' & '+ each.yPos +' & '+ each.yPos);

            // container.spinY(each.yCurrentRotation);
            container.addChild(sensorBox);
            container.addChild(character);
            container.id = character.id;

            playerArrayClient.push(container)

            world.camera.cursor.addChild(container);
          }

          if( each.userId != socket.id) {   //console.log('others...view')

            console.log('others' + each.xPos +' & '+ each.yPos +' & '+ each.yPos);
            container = new Container3D({ x: each.xPos, y: 0, z: each.zPos});   //making container local variable solved it

            // container.addChild(sensorBox);   // don't need on others

            container.addChild(character);
            container.id = character.id;

            playerArrayClient.push(container)
            world.add( container )
          }

          //for displaying how many players online
          currentPlayerNumber = playerArrayClient.length;

      }
    });
    if (playerArrayClient.length > 0) {
      console.log('going to draw map')
      drawMap();
      sensor = new Sensor();
      mapLoaded = true;
    }
  });
}



let pushthis = false;
let pressed = false
let okToMove = false;
let objectAhead
let y;
let a;


function draw() {
    // background(0);


    let changed = false;

    if(mapLoaded == true) {
      if( typeof(sensor) != 'undefined' ){
        // console.log(sensor)
        playerArrayClient.forEach((each) => {
          if (socket.id == each.id) {

            objectAhead = sensor.getEntityInFrontOfUser();
            // console.log(objectAhead,'obj ahead');
          }});
      }
    }

		if (objectAhead && objectAhead.distance < 3.25 && objectAhead.object.el.object3D.el.object3D.userData.treasure) {
			ending = true;
		}
    if (keyIsDown(LEFT_ARROW) && pressed) {

      spinPlayer(1)
      changed = true;

    } if (keyIsDown(RIGHT_ARROW) && pressed) {
      spinPlayer(-1)
      changed = true;

    } if (keyIsDown(UP_ARROW) && pressed ) {  // || mouseIsPressed
      okToMove = true;

      if (objectAhead && objectAhead.distance < 3.25 && objectAhead.object.el.object3D.el.object3D.userData.solid) {
        // okToMove = false;
        console.log("Blocked!");
        nudgeForward(-0.5);
      }

      if (okToMove) {
        changed = true;
        nudgeForward(1);
      }

    } if ( keyIsDown(DOWN_ARROW) && pressed) {

      okToMove = true;

      // if solid object nearby, prevent motion back
      if (objectAhead && objectAhead.distance < 3.25 && objectAhead.object.el.object3D.el.object3D.userData.solid) {
        console.log("Blocked!");
        // okToMove = false;
        nudgeForward(1.5);
      }

      if (okToMove) {
        changed = true;
        nudgeForward(-1);

      }
    }


    if (changed == true) {
      //** emit the new position of THIS character // emit('setPos')

      playerArrayClient.forEach((each) => {
        if (socket.id == each.id) {

          // console.log(each.getWorldPosition().x,'each whatever')
          // map not working - find out why
          // somehow normalize it
          // need to find which block it's currently on -> then get the index of loc, change the number to 7

          // this here will change color based on the number on map


          // console.log('after')
          // console.log(each.getWorldPosition().x,'each whatever')
          // fill(random(255));
          // rect(0,0,20,20);



          if (frameCount % 5 == 0) {
            console.log(each.rotationY,'ROTATION Y VALUE');
            //!important  // should this go 'here mark'
            console.log('socket.emit sendBack_newPos');
            socket.emit('sendBack_newPos', {
              newPosX:each.getWorldPosition().x,
              newPosY:each.getWorldPosition().y,
              newPosZ:each.getWorldPosition().z,
              userId:each.id,
              yCurrentRotation:each.rotationY
            });
          }


        }
      });
      updateMap();
      function updateMap() {
        if(frameCount % 10 == 0 ) {   //// optimizing
          console.log('updating map')
          let currentLocX = map(world.camera.getX(), -worldSize, worldSize, 0, canvasWidth);
          let currentLocZ = map(world.camera.getZ(), -worldSize, worldSize, 0, canvasHeight);

          console.log(currentLocX,'currentLocX')

          for (let r = 0; r < myMap.length; r++) {
            for (let c = 0; c < myMap[r].length; c++) {
              let gridX = r * mapTileWidth;
              let gridY = c * mapTileHeight;
              let nextGridX = gridX + mapTileWidth;
              let nextGridY = gridY + mapTileHeight;

              if (gridX <= currentLocX &&
                  gridY <= currentLocZ &&
                  currentLocX < nextGridX &&
                  currentLocZ < nextGridY) {
                  myMap[c][r] = 7;
              }
              // else if (myMap[c][r] == 7) {
              //   myMap[c][r] = 0;
              // }
              // else {
              //   myMap[c][r] = 0;
              // }

              if ( myMap[c][r] === 7 ) {
                fill('blue');
                rect(r * mapTileWidth, c * mapTileHeight, mapTileWidth, mapTileHeight);

              }
              // if (myMap[c][r] === 0) {
              //   // fill(random(0));
              //   fill(0)
              //   rect(r * mapTileWidth, c * mapTileHeight, mapTileWidth, mapTileHeight);
              // }

            }
          }
         }
        }
      }




    if(playerArrayClient.length > 0) {
      // followMyObject();     //updates camera live
    }

    if (mouseIsPressed) {
      world.moveUserForward(0.05);
      changed = true;

      // world.camera.holder.setAttribute('look-controls', {enabled:true})
    }

    // if (world.camera.holder.hasAttribute('look-controls')) {
    //   // world.camera.holder.removeAttribute('look-controls')
    //   console.log('WE HAVE LOOK CONTROLS ATTRIBUTE')
    // }
    // if (!(world.camera.holder.hasAttribute('look-controls'))) {
    //   console.log('we dont have look-controls')
    // }
} // end of draw

// function mouseReleased() {
//   // world.camera.holder.setAttribute('look-controls', {enabled:false})
//   console.log('mouse released')
// }

function keyPressed() {
  pressed = true;
  // world.camera.holder.removeAttribute('look-controls')
}

// function mousePressed() {
//   world.camera.holder.setAttribute('look-controls', '');
//   // world.camera.holder.setAttribute('look-controls', 'enabled:false');
// }

function mouseClicked() { //mousePressed and mouseClicked is different
  console.log('howmanytimes')
  // loadSetup();
  debugHelper();

	return false;
}


function broadcastFunc() {
  socket.on('broadcast', function(data) {			//recieves the coord and rotation - update live
  console.log('socket.on broadcast');

    playerArrayClient.forEach((each) => {
      if (data.userId == each.id) {
         // each.setPosition(data.xPos,data.yPos,data.zPos)
        if (each.id == socket.id) {
            //it's myself so skip
        } else {
          each.setPosition(data.xPos, data.yPos, data.zPos)
  				each.rotateY(data.yCurrentRotation);					///chek testing - well they gotta face something - even if thats not their camera
        }
      }
    });
  });
}

function nudgeForward(nudgeAmount){
  playerArrayClient.forEach((each) => {
    if (socket.id == each.id) {
      // world.camera.nudgePosition(0,0,1);
      world.moveUserForward(nudgeAmount);

    }
  });
}

function spinPlayer(spinAmount) {
	playerArrayClient.forEach((each) => {
		if (socket.id == each.id) {
			// each.spinY(spinAmount);

      world.camera.rotateY(spinAmount);
      each.rotationY = world.camera.rotationY

      // if (spinAmount > 0) {
      // } else if (spinAmount < 0) {
      // }
		}
	});
}

let infront = 1;
let above = 3;
let shift = 0;
let easing = 0.6;
let shiftTowards;

// function followMyObject() {
//   playerArrayClient.forEach((each) => {
//     if (socket.id == each.id) {
//
//         world.camera.setPosition(each.getX(), each.getY() + above, each.getZ() + infront);
//     }
//   });
// }


// ok your not updating urself


function debugHelper() {
  console.log('frameRate', frameRate());
  console.log('why gthree times')
  socket.emit('debug');
  playerArrayClient.forEach((each, index) => {
    // if(each.id == socket.id) {
      console.log(`player${index}, x: ${each.getX()}, z: ${each.getZ()}, rotated: ${each.rotationY}`);
    // }

  });
  // console.log(playerArrayClient[0].rotationY,'playerArrayClient');
}
//major problem =-=== 1 is overwriting 0// don't know where that is happening


// https://www.npmjs.com/package/aframe-look-at-component

socket.on('disconnect', function(data) {
  //console.log('number of players: ', playerArrayClient.length);

  for (let j = 0; j < playerArrayClient.length; j++) {
    //console.log(playerArrayClient[j].id,'players ids');
      if (playerArrayClient[j].id == data.id) {
        world.remove(playerArrayClient[j]);
        // world.removeChild(container)

        playerArrayClient.splice(j, 1);
        j-=1;
      }
  }
	currentPlayerNumber = playerArrayClient.length
  //console.log(socket.id, 'good bye!');
  //console.log('number of players: ', playerArrayClient.length)
});


class Sensor {
  constructor() {
		// raycaster - think of this like a "beam" that will fire out of the
		// bottom of the user's position to figure out what is below their avatar
		this.rayCaster = new THREE.Raycaster();
		this.userPosition = new THREE.Vector3(0,0,0);
    this.frontVector = new THREE.Vector3(0,0,-1);

		this.rayCasterFront = new THREE.Raycaster();
		this.cursorPosition = new THREE.Vector2(0,0);
		this.intersectsFront = [];
	}

  getEntityInFrontOfUser() {
        var cp = world.getUserPosition();
        this.userPosition.x = cp.x;
        this.userPosition.y = cp.y;
        this.userPosition.z = cp.z;

        if (world.camera.holder.object3D.children.length >= 2) {
          this.rayCasterFront.setFromCamera( this.cursorPosition, world.camera.holder.object3D.children[1]);
          // this.rayCaster.set(this.userPosition, this.frontVector);
          this.intersectsFront = this.rayCasterFront.intersectObjects( world.threeSceneReference.children, true );

        }
        //
          // determine which "solid" items are in front of the user
          for (var i = 0; i < this.intersectsFront.length; i++) {
            if (!this.intersectsFront[i].object.el.object3D.userData.solid) {
              this.intersectsFront.splice(i,1);
              i--;
            }
          }
          // console.log(this.intersectsFront[0].object.el.object3D.userData.solid,'solid?');
          // console.log(this.intersectsFront.length);

          if (this.intersectsFront.length > 0) {
            // console.log(this.intersectsFront[0],'first')
            return this.intersectsFront[0];
          }
          return false;
	}
}

/*********************************************************/
function drawMap() {

  containerMap = new Container3D({x:-worldSize/2, y:0, z:-worldSize/2}); // move to the center

  for (let row = 0; row < myMap.length; row++) {
    for (let col = 0; col < myMap[row].length; col++) {
      console.log(tileSizeWidth,'huh')
      console.log(tileSizeLength,'huh')
      let xPos = col * tileSizeWidth;
      let zPos = row * tileSizeLength;

      let block = new Box({
          x:xPos, y:3, z:zPos,
          opacity: 1,
          width: tileSizeWidth,
          depth: tileSizeLength,
          height: 15,
          asset: 'brick',
          metalness:0.25,   //maybe not
          repeatX: 25,
          repeatY: 50,
          red: random(100,240), green:random(100,240), blue:random(100,240)
      });
      block.tag.object3D.userData.solid = true;


      if ( myMap[row][col] == 3 ) {
        containerMap.addChild(block);
      }
      else if ( myMap[row][col] == 1 ) {
        containerMap.addChild(block);
      }
      else if ( myMap[row][col] == 5 ) {
        treasure = new OBJ({
      		asset: 'treasure',
      		mtl: 'treasure_mtl',
      		x: xPos,
      		y: 3.5,
      		z: zPos,
      		rotationX:0,
      		rotationY:180,
      		scaleX:5,
      		scaleY:5,
      		scaleZ:5,
      	});
        treasureBox = new Box({
          x:xPos, y:3, z:zPos,
          opacity: 0.1,
          width: tileSizeWidth/4,
          depth: tileSizeLength/4,
          height: 1.5,
          red: random(100,240), green:random(100,240), blue:random(100,240),
          clickFunction: function(t) {
            // console.log("Clicked");
            //Ideally we want to move the winning screen
          }
        });
        treasureBox.tag.object3D.userData.solid = true;
				treasureBox.tag.object3D.userData.treasure = true;
        containerMap.addChild(treasure);
        containerMap.addChild(treasureBox);
      }
    }
  }
  world.add(containerMap);
}
