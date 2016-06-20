# NAME:  Grogan Wayne Huff
# UNO ID:  **810
#
# Partners: NONE
#
# Expects program to be executed with a single file.csv system parameter.
# file.csv should be a single column csv of gene identifiers.
# This program has a tendency to disconnect if file.csv is too much longer
#     than 1500-2000 genes at one time. Results may vary in practice.
# Returns 5'-3' upstream base-pairs and a header with the provided gene name.
# You can save output as text by redirecting to a new file
#     $ python requestYeastMine.py file.csv 50 > output.txt

import sys, requests, time
from datetime import date

if (len(sys.argv) == 2 or len(sys.argv) == 3) and str(sys.argv[1]).find(".csv") != -1:
    # set up upstream length, or default to 200 if not given or not an int
    if len(sys.argv) == 2:
        sys.argv.append('200')
    try:
        is_int(sys.argv[2])
    except:
        sys.argv[2] = '200'
    if int(sys.argv[2]) <= 0:
        sys.argv[2] = '200'
    size = int(sys.argv[2])
    # account for \n characters
    size += size/60 - 1 if size%60 == 0 else size/60

    # try to open [file].csv
    try:
        # store in genes[]
        genedata = open('./' + str(sys.argv[1]), 'r')
        genes = [line.strip() for line in genedata]
        genedata.close()

        skipped = []
        try:
            # print a timestamp of when data retrieval was initialized
            print 'Data retrieved:',
            print date.today()

            # powers the requests.get function, seqname will be replaced at
            #     every iteration of the for loop with a line from genes[]
            payload = {'map': 'a3map', 'seqname': ' ',
                'flankl': sys.argv[2], 'flankr': sys.argv[2], 'rev': ''}
            for gene in genes:
                payload['seqname'] = gene
                r = requests.get(
                    'http://yeastgenome.org/cgi-bin/getSeq', params = payload)
                raw = r.text
                # if http return contains a sequence, it will have a <pre> tag.
                #     else, no <pre> tag is returned.
                index = raw.find("<pre>")
                if index != -1:
                    header = raw[raw.find('\n', index + 5) + 1:]
                    seq = header[:size]
                    header = '>'+gene
                    print header
                    print seq
                else:
                    skipped.append(gene)
        except:
            print 'There was an error retriving YeastMine data.'
            print 'Please check your output and resume program with a new .csv',
            print 'file of the remaining genes.'
            print 'Note: Disconnect occurs for files sizes larger larger than',
            print '1500 genes.\n'
        print 'Skipped:'
        for gene in skipped:
            print gene
    except:
        print 'There was an error opening your file.'
else:
    print '\nPlease run as >python requestYeastMine.py [GenesFileName].csv',
    print '[Upstream Length (Defaults to 200)]\n'
