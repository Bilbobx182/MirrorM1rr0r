import os

print(os.getcwd())

with open(os.getcwd() +"\lol.txt","a") as myfile:
    myfile.write("lol")