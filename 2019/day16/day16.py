BASE_PATTERN = [0, 1, 0, -1]
SEVEN = 5971723


def read_input():
    with open("input.txt") as file:
        input = file.readline().strip()
    return input


def calc_input_iter(input):
    new_input = []
    for i in range(1, len(input) + 1):
        # pattern = []
        # for pos in BASE_PATTERN:
        #     pattern += [pos] * i
        # pat_len = len(pattern)

        total = 0
        for ind in range(len(input)):
            total += int(input[ind]) * BASE_PATTERN[((ind + 1) // i) % 4]
        new_input.append(abs(total) % 10)
    return new_input


def part_one(input):
    for i in range(100):
        input = calc_input_iter(input)
    print(input[:8])


def calc_input_iter_one(input):
    # triangular matrix with many zeros
    sum = 0
    for i in range(len(input) - 1, -1, -1):
        sum += int(input[i])
        sum = sum % 10
        input[i] = sum # in place is a lot faster
    return input


def part_two(input):
    input = input * 10000
    input = input[SEVEN:]
    print(len(input))
    for i in range(100):
        input = calc_input_iter_one(input)
    print(input[:8])


def main():
    input = list(read_input())
    # part_one(input)
    part_two(input)


if __name__ == '__main__':
    main()
