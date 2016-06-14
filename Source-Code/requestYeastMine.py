# NAME:  Grogan Wayne Huff
# UNO ID:  **810
#
# Partners: NONE
#
# Expects program to be executed with a single file.csv system parameter.
# file.csv should be a single column csv of gene identifiers.
# This program has a tendency to disconnect if file.csv is too much longer
#     than 2000 genes at one time. Results may vary in practice.
# Returns 200 upstream base-pairs and a header with the provided gene name.

import sys, requests

if len(sys.argv) == 2 and str(sys.argv[1]).find(".csv") != -1:
    try:
        # try to open the "filename".csv file provided, store in genes[]
        genedata = open('./' + str(sys.argv[1]), 'r')
        genes = [line.strip() for line in genedata]
        genedata.close()
        
        # powers the requests.get function, seqname will be replaced at
        #     every iteration of the for loop with a line from genes[]
        payload = {'map': 'a3map', 'seqname': ' ',
            'flankl': '200', 'flankr': '200', 'rev': ''}
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
                seq = header[:203]
                header = '>'+gene
                # You can save this output by redirecting to a new file
                # $ python requestYeastMine.py file.csv > output.txt
                print header
                print seq
    except:
        print 'There was an error.'
else:
    print '\nPlease run as >python requestYeastMine.py\
 FileNameOfUniqueGenes.csv\n'

