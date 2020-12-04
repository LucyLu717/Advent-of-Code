import re

NUMBER_PATTERN = r"[0-9]{4}"

fields = {
    "byr": lambda info: re.match(NUMBER_PATTERN, info) and 1920 <= int(info) <= 2002,
    "iyr": lambda info: re.match(NUMBER_PATTERN, info) and 2010 <= int(info) <= 2020,
    "eyr": lambda info: re.match(NUMBER_PATTERN, info) and 2020 <= int(info) <= 2030,
    "hgt": lambda info: info[-2:] == "cm"
    and 150 <= int(info[:-2]) <= 193
    or info[-2:] == "in"
    and 59 <= int(info[:-2]) <= 76,
    "hcl": lambda info: re.match(r"^#([0-9a-f]{6})$", info),
    "ecl": lambda info: info in ["amb", "blu", "brn", "gry", "grn", "hzl", "oth",],
    "pid": lambda info: re.match(r"^[0-9]{9}$", info),
    "cid": lambda info: True,
}


def check_passport():
    valid_fields = 0
    valid = 0
    with open("../inputs/day4.txt") as f:
        passports = [[]]
        for line in f:
            line = line.strip()
            if len(line) < 1:
                passport = passports[-1]
                present_fields = set()
                valid_field = True
                for item in passport:
                    field = item.split(":")[0]
                    present_fields.add(field)
                    info = item.split(":")[1]
                    valid_field = valid_field and fields[field](info)
                present_fields.add("cid")
                present_fields.remove("cid")
                # if fields ^ present_fields == set([]) or fields ^ present_fields == set(
                #     ["cid"]
                # ):
                if len(present_fields) == 7:
                    valid += 1
                    if valid_field:
                        valid_fields += 1
                passports.append([])
            else:
                passports[-1].extend(line.split(" "))
    print(valid, valid_fields)


def main():
    check_passport()


if __name__ == "__main__":
    main()
