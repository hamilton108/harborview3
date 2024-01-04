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

APPS = { 1: "charts",
         2: "optionpurchase"
    }

PROJ = "/home/rcs/opt/java/harborview3"

TARGET = "%s/src/main/resources/static/js/maunaloa" % PROJ

SRC = "%s/purescript/dist" % PROJ

def md5_sum(src_file):
    with open(src_file, "r") as fx:
        content = fx.read()
    tmp = hashlib.md5(content.encode())
    result = tmp.hexdigest() # [0:10]
    return result

class Application:
    def __init__(self, app_id, main_module) -> None:
        self.main_module = main_module 
        self.app_id = app_id
        self.app_name = APPS[app_id]
        self._md5sum = None

        if app_id == 1:
            self.tpl = "charts.html.tpl"
            self.html = "charts.html"
        else:
            self.tpl = "optionpurchase.html.tpl"
            self.html = "optionpurchases.html"


    @property
    def src_file(self):
        return "ps-%s.js" % self.app_name

    @property 
    def build_target(self):
        return "dist/%s" % self.src_file

    @property
    def src(self):
        return "%s/%s" % (SRC,self.src_file)

    @property
    def md5sum(self):
        if self._md5sum == None:
            self._md5sum = md5_sum(self.src)[0:8]
        return self._md5sum 

    @property 
    def map_file_name(self):
        return "%s.map" % self.src_file

    @property
    def md5_file_name(self):
        return "ps-%s-%s.js" %  (self.app_name,self.md5sum)
       
    def build(self):
        proc.run(["spago", "bundle", "--source-maps", "--module", self.main_module, "--outfile", self.build_target])

    def copy(self):
        copyfile(self.src,"%s/%s" % (TARGET,self.md5_file_name))
        copyfile("%s/%s" % (SRC,self.map_file_name), "%s/%s" % (TARGET,self.map_file_name))

    def minify(self):
        pass

    def render(self):
        tpl = Template(filename="%s/%s" % (TPL_SRC,self.tpl))
        result = tpl.render(psname=self.md5_file_name)
        dist = "%s/%s" % (TPL_DIST,self.html)
        with(open(dist, "w")) as f:
            f.write(result)

JS_SRC = "%s/ps-charts.js" % SRC

JS_MIN = "%s/ps-charts.min.js" % SRC

JS_TARGET = "%s/ps-charts.js" % TARGET

TPL_SRC = "%s/python/templates" % PROJ

TPL_DIST = "%s/src/main/resources/templates/maunaloa" % PROJ

def build():
    # proc.run(["spago", "bundle-app", "--main", "Main", "--to", JS_SRC])
    proc.run(["spago", "bundle", "--source-maps", "--module", "Main", "--outfile", JS_SRC])

def minify():
    proc.run(["esbuild", JS_SRC, "--minify", "--outfile=%s" % JS_MIN])

    
def md5_file_name_(src_file):
    #stem = os.path.basename(src_file).split(".")
    return "ps-charts-%s.js" %  md5_sum(src_file)

# def render(mfn):
#     tpl = Template(filename="%s/charts.html.tpl" % TPL_SRC)
#     result = tpl.render(pscharts=mfn)
#     dist = "%s/charts.html" % TPL_DIST
#     with(open(dist, "w")) as f:
#         f.write(result)

# def versioning(do_build):
#     if do_build == True:
#         build()    
#     mfn = md5_sum(JS_SRC)
#     print(mfn)
#     copyfile(JS_SRC,"%s/%s" % (TARGET,"ps-charts.js"))
#     render_charts(mfn)

# def md5_file_name(do_build):
#     if do_build == True:
#         build()    
#     mfn = md5_file_name_(JS_SRC)
#     print(mfn)
#     copyfile(JS_SRC,"%s/%s" % (TARGET,mfn))
#     render_charts(mfn)

if __name__ == '__main__':
    parser = OptionParser()
    parser.add_option("--build", action="store_true", default=False,
                      help="Build module. Default: False")
    parser.add_option("--min", action="store_true", default=False,
                      help="Minify js file. Default: False")
    parser.add_option("--md5file", action="store_true", default=False,
                      help="Save md5 sum in filename (instead of versioning). Default: False")
    parser.add_option("--md5", action="store_true", default=False,
                      help="Check md5 sum of dist/ps-charts.js. Default: False")
    parser.add_option("--app", dest="app", action="store", type="int",
                      metavar="APP", help="App name: 1: Maunaloa, 2: OptionPurchase")
    (opts, args) = parser.parse_args()

    if opts.app == 1:
        cur_app = Application(1,"Main") 
    else:
        cur_app = Application(2,"OptionPurchaseMain") 

    if opts.build == True:
        #cur_app.build()
        #cur_app.render()
        cur_app.copy()

    print (cur_app.src)
    print (cur_app.md5_file_name)


    # if opts.md5 == True:
    #     print (md5_sum(JS_SRC))
    #     print (md5_sum(JS_TARGET))
    # else:
    #     if opts.md5file == True:
    #         md5_file_name(opts.build)
    #     else:
    #         versioning(opts.build)
    