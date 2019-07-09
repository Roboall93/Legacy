import math

photos = {}
tags = {}
co_occur = {}

def read_lines(path):
    splitlines = []
    for l in open(path).read().splitlines():
        splitlines.append(l.split(","))
    return splitlines

def top_five(d):
    top = []
    for w in sorted(d, key=d.get, reverse=True):
        top.append([w, d[w]])
    return top[:5]

def top_co_occur(tag):
    return top_five(co_occur[tag])

def top_idf(tag):
    d = co_occur[tag]
    for w in d:
        d[w] *= math.log(tags[w][1])
    return top_five(d)

for line in read_lines("photos.csv"):
    photos[line[0]] = []

for line in read_lines("tags.csv"):
    tags[line[0]] = [line[1], 0]

for line in read_lines("photos_tags.csv"):
    photos[line[0]].append(line[1])

photos_size = len(photos)
for t in tags:
    tags[t][1] = photos_size / int(tags[t][0])

for t1 in tags:
    co_occur[t1] = {}
    for t2 in tags:
        co_occur[t1][t2] = 0

for p in photos:
    t = photos[p]
    for t1 in t:
        for t2 in t:
            if t1 != t2:
                co_occur[t1][t2] += 1

with open("out.csv", "w") as o:
    o.write("," + ','.join(co_occur.keys()) + "\n")
    for key in co_occur:
        o.write(key + "," + ','.join(map(str, co_occur[key].values())) + "\n")

print(top_co_occur("sky"))
print(top_co_occur("water"))
print(top_co_occur("people"))
print(top_co_occur("london"))
print(top_idf("water"))
print(top_idf("people"))
print(top_idf("london"))
