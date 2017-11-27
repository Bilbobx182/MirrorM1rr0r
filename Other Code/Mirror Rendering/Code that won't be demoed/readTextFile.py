with open('info.txt') as f:
    content = f.readlines()
contents = [line.strip() for line in content]

print(contents)
