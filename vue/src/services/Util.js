export default {
    formatToMoney(num) {
        let money = num.toString();

        if (money.length === 1) {
            money = "00" + money
        } else if (money.length === 2) {
            money = "0" + money;
        }

        return money.substring(0, money.length - 2) + "." + money.substring(money.length - 2);
    }
}