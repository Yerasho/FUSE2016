import subprocess
# In the event that a previous failure ended program prematurely
#  seek user input for file number start and stop. 
n = input('file start (int): ')
# Smallest file number allowed is 0file.csv
if n < 0:
    n = 0
    print ('Assert: !(n < 0). Setting n = 0')
m = input('file stop (int): ')
for i in range(n,m+1):
    # File naming convention from ProcessCompletek3.java
    sourcefile = str(i)+"file.csv"
    # meme suite must be install seperately
    ps = subprocess.Popen(["meme", sourcefile, "-text", "-dna", "-nmotifs",
        "1", "-mod", "zoops", "-minw", "6", "-maxw", "20", "-nostatus"],
        stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    o, e = ps.communicate()
    # o : subprocess.stdout
    # e : subprocess.stderr
    f = open(sourcefile, "a")
    print (i)
    if o.find("sites =   3") != -1:
        # if the first motif is shared in 3 sites
        f.write("true\n")
    else:
        if not e:
            # else, if no error
            f.write("false\n")
        else:
            # else indicate error
            print (e[:-1])
            f.write("error\n")
    f.close()
