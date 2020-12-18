op_param_map = {1: 3, 2: 3, 3: 1, 4: 1, 5: 2, 6: 2, 7: 3, 8: 3, 99: 0}
INPUT = 5


def main():
    with open("input.txt") as file:
        row = file.readline()
        numbers = row.split(",")
    for i in range(len(numbers)):
        numbers[i] = int(numbers[i])
    intcode(numbers)


def intcode(numbers):
    pointer = 0

    while True:
        op, param_mode, param_num = decode_insn(numbers[pointer])
        if op == 1:
            rs1, rs2 = get_rs(param_mode, numbers, pointer)
            numbers[numbers[pointer + 3]] = rs1 + rs2
        elif op == 2:
            rs1, rs2 = get_rs(param_mode, numbers, pointer)
            numbers[numbers[pointer + 3]] = rs1 * rs2
        elif op == 3:
            numbers[numbers[pointer + 1]] = INPUT
        elif op == 4:
            rs1 = numbers[pointer + 1]
            if param_mode[0] == 0:
                rs1 = numbers[rs1]
            print(rs1)
        elif op == 5:
            rs1, rs2 = get_rs(param_mode, numbers, pointer)
            if rs1 != 0:
                pointer = rs2
                continue
        elif op == 6:
            rs1, rs2 = get_rs(param_mode, numbers, pointer)
            if rs1 == 0:
                pointer = rs2
                continue
        elif op == 7:
            rs1, rs2 = get_rs(param_mode, numbers, pointer)
            numbers[numbers[pointer + 3]] = 1 if rs1 < rs2 else 0
        elif op == 8:
            rs1, rs2 = get_rs(param_mode, numbers, pointer)
            numbers[numbers[pointer + 3]] = 1 if rs1 == rs2 else 0
        elif op == 99:
            break
        pointer += param_num + 1


def get_rs(param_mode, numbers, pointer):
    rs1 = numbers[pointer + 1]
    if param_mode[len(param_mode) - 1] == 0:
        rs1 = numbers[rs1]
    rs2 = numbers[pointer + 2]
    if param_mode[len(param_mode) - 2] == 0:
        rs2 = numbers[rs2]
    return rs1, rs2


def decode_insn(num):
    opcode = num % 100
    num = num // 100
    param_mode = []
    param_num = op_param_map.get(opcode)
    for i in range(param_num):
        param_mode.insert(0, num % 10)
        num = num // 10
    return opcode, param_mode, param_num


if __name__ == '__main__':
    main()