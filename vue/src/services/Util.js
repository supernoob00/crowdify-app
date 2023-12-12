export default {
    formatToMoney(num) {
        let money = num.toString();

        if (money.length === 1) {
            money = "00" + money
        } else if (money.length === 2) {
            money = "0" + money;
        }

        const formattedMoney = money.substring(0, money.length - 2) + "." + money.substring(money.length - 2)
        if (formattedMoney.endsWith('.00')) {
            return formattedMoney.substring(0, formattedMoney.length - 3)
        }
        return formattedMoney;
    }
}