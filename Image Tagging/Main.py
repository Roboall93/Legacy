import math

'''
In this program we read in photo and tag details from provided csv files (in same directory)
We then produce a co-occurrence table and write it to an output file 'co_occur.txt'
Then print out top five tags and idf tags for relevant tags

NOTE: This program REQUIRES the csv files to be in the same directory as this file
'''

# set up variables
photos = {}  # dictionary of each photo with list of tags
tags = {}  # dictionary of tags and I/I(x) values
co_occur = {}  # co-occurrence dictionary of dictionaries for lookup


# call to read lines from input file one at a time
def read_line(path):
    splitlines = []
    for l in open(path, errors='ignore').read().splitlines():
        splitlines.append(l.split(",")) # split on commas and append
    return splitlines


# calculate top five suggested tags
def get_suggested_tags(d):
    top = []
    for w in sorted(d, key=d.get, reverse=True): #iterate through sorted reversed list
        top.append([w, d[w]])
    return top[:5] # return top five values


# get dictionary entry from the tag
def top_five_tags(tag):
    return get_suggested_tags(co_occur[tag]) # pass co occurrence entry


# calculate top five tags based on idf value
def top_five_idf(tag):
    d = co_occur[tag]  # get tag co-occurrence values
    return get_suggested_tags(d)


# print the top values from the list
def parse_top_five(taglist):
    for x in taglist:
        print("%s %s" % (x[0], x[1]))


# print idf with only two decimal places
def parse_idf(taglist):
    for x in taglist:
        print("%s %.2f" % (x[0], x[1]))


# start here
# read in csv files
for line in read_line("photos.csv"):
    photos[line[0]] = []

for line in read_line("tags.csv"):
    tags[line[0]] = [line[1], 0]

for line in read_line("photos_tags.csv"):
    photos[line[0]].append(line[1])

# set up tags dictionary with I/I(X) values
photos_size = len(photos)
for t in tags:
    tags[t][1] = photos_size / int(tags[t][0])  # number of photos with this tag

# set up empty dict of dicts for co-occurrence
for t1 in tags:
    co_occur[t1] = {}
    for t2 in tags:
        co_occur[t1][t2] = 0

# calculate co-occurrence values
for p in photos:
    t = photos[p]
    for t1 in t:
        for t2 in t:
            if t1 != t2:
                co_occur[t1][t2] += 1


# write co-occurrence table to output file
with open("co_occur.txt", "w") as f:
    f.write(','.join(co_occur.keys()) + "\n")  # top row of key values
    for key in co_occur:
        f.write(key + "," + ','.join(map(str, co_occur[key].values())) + "\n")
        #  Creates a sequence of co-occurrence values using the associated tag and converts each value to a string.
        # After this, we concatenate each value to a comma delimited string"


topfivetags = [top_five_tags("water"), top_five_tags("people"), top_five_tags("london"), top_five_tags("sky")]

#now write in TF-IDF values
for tag in tags:
    d = co_occur[tag]  # get tag co-occurrence values
    for w in d:
        d[w] *= math.log(tags[w][1])  # calculate idf value for each tag


topidftags = [top_five_idf("water"), top_five_idf("people"), top_five_idf("london"), top_five_idf("sky")]


# print relevant data
print("Water\nSuggested Tags: ")
parse_top_five(topfivetags[0])
print("\nRelevant Tags (TF-IDF):")
parse_idf(topidftags[0])
print("\nPeople\nSuggested Tags: ")
parse_top_five(topfivetags[1])
print("\nRelevant Tags (TF-IDF):")
parse_idf(topidftags[1])
print("\nLondon\nSuggested Tags: ")
parse_top_five(topfivetags[2])
print("\nRelevant Tags (TF-IDF):")
parse_idf(topidftags[2])


