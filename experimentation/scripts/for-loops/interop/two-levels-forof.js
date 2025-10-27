"use strict";

function main() {
	const list1 = filledArray(150, 0);
	const list2 = filledArray(150, 1);
	const list3 = filledArray(150, 2);
		
	list1[list1.length-1].id = -100;
	list3[list3.length-1].id = -100;

	if (!forof(list1, list2, list3)) {
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
 
function forof(left, right, noneof) {
    for (let leftValue of left) {
        for (let rightValue of right) {
			if (leftValue.id == rightValue.id) {
				return true;
			}
        }
		
		for (let noneValue of noneof) {
			if (leftValue.id == noneValue.id) {
				return true;
			}
        }
    }
    return false;
}
