#!/usr/bin/python3

#from cmath import exp
from optparse import OptionParser
#from os.path import (isfile, getmtime, basename)
import os.path 

from mako.template import Template

import subprocess as proc
from shutil import copyfile
import hashlib

"""
# initializing string
str2hash = "GeeksforGeeks"
 
# encoding GeeksforGeeks using encode()
# then sending to md5()
result = hashlib.md5(str2hash.encode())
 
# printing the equivalent hexadecimal value.
print("The hexadecimal equivalent of hash is : ", end ="")
print(result.hexdigest())
"""

PROJ = "/home/rcs/opt/java/harborview3"

SRC = "%s/purescript/dist" % PROJ

TARGET = "%s/src/main/resources/static/js/maunaloa" % PROJ

JS_SRC = "%s/ps-charts.js" % SRC

JS_MIN = "%s/ps-charts.min.js" % SRC

PS_HOME = "/home/pureuser"

PS_SRC = "%s/dist/ps-charts.js" % PS_HOME

PS_MIN = "%s/dist/ps-charts.min.js" % PS_HOME

TPL_SRC = "%s/python/templates" % PROJ

TPL_DIST = "%s/src/main/resources/templates/maunaloa" % PROJ

def build():
    proc.run(["spago", "bundle-app", "--main", "Main", "--to", PS_SRC])

def minify():
    proc.run(["esbuild", PS_SRC, "--minify", "--outfile=%s" % PS_MIN])

def md5_sum(src_file):
    with open(src_file, "r") as fx:
        content = fx.read()
    tmp = hashlib.md5(content.encode())
    result = tmp.hexdigest() # [0:10]
    return result
    
def md5_file_name(src_file):
    #stem = os.path.basename(src_file).split(".")
    return "ps-charts-%s.js" %  md5_sum(src_file)

def render_charts(mfn):
    tpl = Template(filename="%s/charts.html.tpl" % TPL_SRC)
    result = tpl.render(pscharts=mfn)
    dist = "%s/charts.html" % TPL_DIST
    with(open(dist, "w")) as f:
        f.write(result)

def versioning(do_build):
    if do_build == True:
        build()    
    mfn = md5_sum(JS_SRC)
    print(mfn)
    copyfile(JS_SRC,"%s/%s" % (TARGET,"ps-charts.js"))
    render_charts(mfn)

def md5_file_name(do_build):
    if do_build == True:
        build()    
    mfn = md5_file_name(JS_SRC)
    print(mfn)
    copyfile(JS_SRC,"%s/%s" % (TARGET,mfn))
    render_charts(mfn)

if __name__ == '__main__':
    parser = OptionParser()
    parser.add_option("--build", action="store_true", default=False,
                      help="Build module. Default: False")
    parser.add_option("--min", action="store_true", default=False,
                      help="Minify js file. Default: False")
    parser.add_option("--md5file", action="store_true", default=False,
                      help="Save md5 sum in filename (instead of versioning). Default: False")
    (opts, args) = parser.parse_args()

    if opts.md5file == True:
        md5_file_name(opts.build)
    else:
        versioning(opts.build)
    