def main():
    with open("input.txt") as file:
        total = 0
        row = file.readline()
        while row is not None and len(row) > 0:
            fuel = int(row)
            while True:
                fuel = fuel // 3 - 2
                if fuel > 0:
                    total += fuel
                else:
                    break
            row = file.readline()
        print(total)


if __name__ == '__main__':
    main()