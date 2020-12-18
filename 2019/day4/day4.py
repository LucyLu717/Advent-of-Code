LOWER = 172851
UPPER = 675869


def main():
    count_one = 0
    count_two = 0
    for i in range(LOWER, UPPER + 1):
        digits = get_digits(i)
        if digits == sorted(digits) and has_double(digits):
            count_one += 1
            if has_exact_double(digits):
                count_two += 1
    print(count_one)
    print(count_two)


def get_digits(num):
    digits = []
    while num > 0:
        digit = num % 10
        digits.insert(0, digit)
        num //= 10
    return digits


def has_double(digits):
    for i in range(0, len(digits) - 1):
        if digits[i] == digits[i + 1]:
            return True
    return False


def has_exact_double(digits):
    count_map = dict()
    for i in range(0, len(digits) - 1):
        count_map[digits[i]] = count_map.get(digits[i]) if digits[i] in count_map else 1
        if digits[i] == digits[i + 1]:
            count_map[digits[i]] += 1
    for i in count_map.keys():
        if count_map.get(i) == 2:
            return True
    return False


if __name__ == '__main__':
    main()