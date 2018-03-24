import struct

colour = '561044'
expected = (0.33725490196078434, 0.06274509803921569, 0.26666666666666666, 1)


fullRGB = struct.unpack('BBB', bytes.fromhex(colour))
RGB = []

for colour in fullRGB:
    RGB.append(colour / 255)
RGB.append(1)

print(RGB)