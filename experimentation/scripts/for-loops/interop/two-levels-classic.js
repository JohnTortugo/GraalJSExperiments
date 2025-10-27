"use strict";

function main() {
	const list1 = filledArray(150, 0);
	const list2 = filledArray(150, 1);
	const list3 = filledArray(150, 2);
		
	list1[list1.length-1].id = -100;
	list3[list3.length-1].id = -100;

	if (!classic(list1, list2, list3)) {
		console.log("Warning. Did not find element!");
	}
}

function filledArray(size, constant) {
	const OrderArray  = Java.type("com.jtortugo.graaljs.Order[]");
	const Order       = Java.type("com.jtortugo.graaljs.Order");
	const array       = new OrderArray(size);
	for (let i=0; i<size; i++) {
		array[i] = new Order(constant);	
	}
	return array;
}
 
function classic(left, right, noneof) {
    let ll = left.length;
    let lr = right.length;
    let ln = noneof.length;

    for (let i=0; i<ll; i++) {
        let leftValue = left[i];
        for (let j=0; j<lr; j++) {
            let rightValue = right[j];
            if (leftValue.id == rightValue.id) {
                return true;
            }
        }
		
		for (let j=0; j<ln; j++) {
            let noneValue = noneof[j];
            if (leftValue.id == noneValue.id) {
                return true;
            }
        }
    }

    return false;
}
 
