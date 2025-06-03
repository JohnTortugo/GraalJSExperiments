function main() {
    const left = new Array(1_000).fill(0);
    const right = new Array(1_000).fill(1);
    const noneof = new Array(1_000).fill(2);
    return classic(left, right, noneof);
}
 
function classic(left, right, noneof) {
    let ll = left.length;
    let lr = right.length;
    let ln = noneof.length;

    for (let i=0; i<ll; i++) {
        let leftValue = left[i];
        for (let j=0; j<lr; j++) {
            let rightValue = right[j];
            if (leftValue.valueOf() == rightValue.valueOf()) {
                return true;
            }
        }
		
		for (let j=0; j<ln; j++) {
            let noneValue = noneof[j];
            if (leftValue.valueOf() == noneValue.valueOf()) {
                return true;
            }
        }
    }

    return false;
}
 