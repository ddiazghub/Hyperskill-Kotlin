class BankAccount(var deposited: Long, var withdrawn: Long) {
    var balance: Long = deposited - withdrawn
}