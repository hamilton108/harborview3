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

TARGET = "%s/src/main/resources/static/js" % PROJ

SRC = "%s/purescript/dist" % PROJ

TPL_DIST = "%s/src/main/resources/templates" % PROJ

TPL_SRC = "%s/python/templates" % PROJ

CSS_HOME = "%s/src/main/resources/static/css" % PROJ

CSS_SRC = "%s/harborview.css" % CSS_HOME 

def md5_sum(src_file):
    with open(src_file, "r") as fx:
        content = fx.read()
    tmp = hashlib.md5(content.encode())
    result = tmp.hexdigest() # [0:10]
    return result

PKG = 1
TPL = 2
HTML = 3
MAIN = 4
JS = 5

APPS = { 1: { PKG: "maunaloa", TPL: "charts.html.tpl", HTML: "charts.html", MAIN: "Main", JS: "charts" },
         2: { PKG: "optionpurchase", TPL: "optionpurchase.html.tpl", HTML: "optionpurchases.html", MAIN: "OptionPurchaseMain", JS: "optionpurchase" },
}

class Application:
    def __init__(self, app_id) -> None:
        app_info = APPS[app_id]
        self.main_module = app_info[MAIN]
        self.pkg = app_info[PKG]
        self.tpl = app_info[TPL]
        self.html = app_info[HTML]
        self.js = app_info[JS]
        self._md5sum = None

    @property
    def src_file(self):
        return "ps-%s.js" % self.js

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
        return "ps-%s-%s.js" %  (self.js,self.md5sum)
       
    def build(self):
        proc.run(["spago", "bundle", "--package", self.pkg, "--source-maps", "--module", self.main_module, "--outfile", self.build_target])

    def copy(self):
        copyfile(self.src,"%s/%s/%s" % (TARGET,self.pkg,self.md5_file_name))
        copyfile("%s/%s" % (SRC,self.map_file_name), "%s/%s/%s" % (TARGET,self.pkg,self.map_file_name))

    def minify(self):
        #proc.run(["esbuild", JS_SRC, "--minify", "--outfile=%s" % JS_MIN])
        pass

    def render(self):
        tpl = Template(filename="%s/%s/%s" % (TPL_SRC,self.pkg,self.tpl))
        result = tpl.render(psname=self.md5_file_name)
        dist = "%s/%s/%s" % (TPL_DIST,self.pkg,self.html)
        with(open(dist, "w")) as f:
            f.write(result)

def sass():
    proc.run(["sass", "../sass-src/harborview.scss", CSS_SRC])
    tpl = Template(filename="%s/%s" % (TPL_SRC,"head.html.tpl"))
    md5 = md5_sum(CSS_SRC)[:8]
    md5_fname = "harborview-%s.css" % md5
    print (md5_fname)
    result = tpl.render(psname=md5_fname)
    dist = "%s/%s" % (TPL_DIST, "head.html")
    with(open(dist, "w")) as f:
        f.write(result)
    copyfile(CSS_SRC, "%s/%s" % (CSS_HOME,md5_fname))


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
    parser.add_option("--sass", action="store_true", default=False,
                      help="Sass. Default: False")
    parser.add_option("--build", action="store_true", default=False,
                      help="Build module. Default: False")
    parser.add_option("--render", action="store_true", default=False,
                      help="Render html templates. Default: False")
    parser.add_option("--min", action="store_true", default=False,
                      help="Minify js file. Default: False")
    parser.add_option("--app", dest="app", action="store", type="int",
                      metavar="APP", help="App name: 1: Maunaloa, 2: OptionPurchase")
    (opts, args) = parser.parse_args()

    if opts.sass == True:
        sass()
    else:
        if opts.app == 1:
            cur_app = Application(1) 
        else:
            cur_app = Application(2) 

        if opts.build == True:
            cur_app.build()
            cur_app.render()
            cur_app.copy()
            print (cur_app.src)
            print (cur_app.md5_file_name)
        elif opts.render == True:
            cur_app.render()
            cur_app.copy()



    # if opts.md5 == True:
    #     print (md5_sum(JS_SRC))
    #     print (md5_sum(JS_TARGET))
    # else:
    #     if opts.md5file == True:
    #         md5_file_name(opts.build)
    #     else:
    #         versioning(opts.build)
    