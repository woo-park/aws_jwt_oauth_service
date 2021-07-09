/* user global var */
let avatar;
let container;


/* world global var  */
let world;
let worldSize = 144;
let canvasWidth = 150;
let canvasHeight = 150;

function setup() {

    createCanvas(canvasWidth, canvasHeight);
    world = new World('VRScene');

    // user POV
    avatar = new Box({
        red: 255, green: 255, blue: 255,
        width: 0.1, height: 0.1, depth: 0.1,
        z: -0.5, y: -0.5
    });

    // add the container to the user's HUD cursor
    world.camera.cursor.addChild(avatar);
    world.camera.holder.setAttribute('wasd-controls','enabled:false');


    // let ground = new Plane({x:0, y:0, z:0, width:worldSize, height:worldSize, rotationX:-90, metalness:0.25, asset:'asphalt'});
    // ground.tag.object3D.userData.solid = true;
    // world.add(ground);


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
    character.id = "68686868"

    let sensorBox = new Box({
        x: 0,
        y: 1,
        z: 0,
        opacity: 0.2,
        red: random(100,240), green:random(100,240), blue:random(100,240)
    });


    container = new Container3D({ x: 0, y: -2, z: 0});

    container.addChild(sensorBox);
    container.addChild(character);
    container.id = character.id;

    world.camera.cursor.addChild(container);

    sensor = new Sensor();
} // end of setup

/* draw global var */
let changed = false;
let pressed = false;
let okToMove = false;
let objectAhead;
let sensor;



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


function draw() {

    if( typeof(sensor) != 'undefined' ){
        objectAhead = sensor.getEntityInFrontOfUser();
    }

    if (keyIsDown(LEFT_ARROW) && pressed) {

        spinPlayer(1)
        changed = true;

    }

    if (keyIsDown(RIGHT_ARROW) && pressed) {
        spinPlayer(-1)
        changed = true;

    }
    //
    if (keyIsDown(UP_ARROW) && pressed ) {  // || mouseIsPressed
        okToMove = true;
    //
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

    if (mouseIsPressed) {
        world.moveUserForward(0.05);
        changed = true;

        // world.camera.holder.setAttribute('look-controls', {enabled:true})
    }

} // end of draw



function spinPlayer(spinAmount) {
    world.camera.rotateY(spinAmount);
    container.rotationY = world.camera.rotationY

}

function keyPressed() {
    pressed = true;
}

function nudgeForward(nudgeAmount){
    world.moveUserForward(nudgeAmount);
}