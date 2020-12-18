def read_input():
    numbers = []
    with open("../inputs/day1.txt") as f:
        for line in f:
            numbers.append(int(line.strip()))
    return numbers


def two_sum(numbers):
    num_set = set(numbers)
    for num in numbers:
        if 2020 - num in num_set:
            print(num * (2020 - num))
            return


def three_sum(numbers):
    num_set = set(numbers)
    for idx1, num1 in enumerate(numbers):
        for idx2, num2 in enumerate(numbers):
            if idx1 != idx2 and 2020 - num1 - num2 in num_set:
                print(num1 * num2 * (2020 - num1 - num2))
                return


def main():
    numbers = read_input()
    two_sum(numbers)
    three_sum(numbers)


if __name__ == "__main__":
    main()
