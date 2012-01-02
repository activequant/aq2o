window.google = window["google"] || {};google.friendconnect = google.friendconnect_ || {};if (!window['__ps_loaded__']) {/*http://www-a-fc-opensocial.googleusercontent.com/gadgets/js/rpc:core.util.js?c=1*/
window['___jsl'] = window['___jsl'] || {};(window['___jsl']['ci'] = (window['___jsl']['ci'] || [])).push({"rpc":{"commSwf":"//xpc.googleusercontent.com/gadgets/xpc.swf","passReferrer":"p2c:query","parentRelayUrl":"/rpc_relay.html"}});window['___jsl'] = window['___jsl'] || {};window['___jsl']['u'] = 'http:\/\/www-a-fc-opensocial.googleusercontent.com\/gadgets\/js\/rpc:core.util.js?c=1';window['___jsl']['f'] = ['rpc','core.util'];window['___jsl']['ms'] = 'https://apis.google.com';(window['___jsl']['ci'] = (window['___jsl']['ci'] || [])).push({"rpc":{"commSwf":"//xpc.googleusercontent.com/gadgets/xpc.swf","passReferrer":"p2c:query","parentRelayUrl":"/rpc_relay.html"}});
/* [start] feature=gapi-globals */
var gapi=window.gapi||{};
;

/* [end] feature=gapi-globals */

/* [start] feature=globals */
var gadgets=window.gadgets||{},shindig=window.shindig||{},osapi=window.osapi=window.osapi||{},google=window.google||{};
;

/* [end] feature=globals */

/* [start] feature=taming */
var safeJSON=window.safeJSON;
var tamings___=window.tamings___||[];
var bridge___;
var caja___=window.caja___;
var ___=window.___;;

/* [end] feature=taming */

/* [start] feature=core.config.base */
window['___cfg'] = window['___cfg'] || window['___gcfg'];;
if(!window.gadgets["config"]){gadgets.config=function(){var f;
var h={};
var b={};
function c(j,l){for(var k in l){if(!l.hasOwnProperty(k)){continue
}if(typeof j[k]==="object"&&typeof l[k]==="object"){c(j[k],l[k])
}else{j[k]=l[k]
}}}function i(){var j=document.scripts||document.getElementsByTagName("script");
if(!j||j.length==0){return null
}var m;
if(f.u){for(var k=0;
!m&&k<j.length;
++k){var l=j[k];
if(l.src&&l.src.indexOf(f.u)==0){m=l
}}}if(!m){m=j[j.length-1]
}if(!m.src){return null
}return m
}function a(j){var k="";
if(j.nodeType==3||j.nodeType==4){k=j.nodeValue
}else{if(j.innerText){k=j.innerText
}else{if(j.innerHTML){k=j.innerHTML
}else{if(j.firstChild){var l=[];
for(var m=j.firstChild;
m;
m=m.nextSibling){l.push(a(m))
}k=l.join("")
}}}}return k
}function e(k){if(!k){return{}
}var j;
while(k.charCodeAt(k.length-1)==0){k=k.substring(0,k.length-1)
}try{j=(new Function("return ("+k+"\n)"))()
}catch(l){}if(typeof j==="object"){return j
}try{j=(new Function("return ({"+k+"\n})"))()
}catch(l){}return typeof j==="object"?j:{}
}function g(n){var p=window.___cfg;
if(p){c(n,p)
}var o=i();
if(!o){return
}var k=a(o);
var j=e(k);
if(f.f&&f.f.length==1){var m=f.f[0];
if(!j[m]){var l={};
l[f.f[0]]=j;
j=l
}}c(n,j)
}function d(o){for(var l in h){if(h.hasOwnProperty(l)){var n=h[l];
for(var m=0,k=n.length;
m<k;
++m){o(l,n[m])
}}}}return{register:function(l,k,j,m){var n=h[l];
if(!n){n=[];
h[l]=n
}n.push({validators:k||{},callback:j,callOnUpdate:m})
},get:function(j){if(j){return b[j]||{}
}return b
},init:function(k,j){f=window.___jsl||{};
c(b,k);
g(b);
var l=window.___config||{};
c(b,l);
d(function(q,p){var o=b[q];
if(o&&!j){var m=p.validators;
for(var n in m){if(m.hasOwnProperty(n)){if(!m[n](o[n])){throw new Error('Invalid config value "'+o[n]+'" for parameter "'+n+'" in component "'+q+'"')
}}}}if(p.callback){p.callback(b)
}})
},update:function(k,o){var n=[];
d(function(p,j){if(k.hasOwnProperty(p)||(o&&b&&b[p])){if(j.callback&&j.callOnUpdate){n.push(j.callback)
}}});
b=o?{}:b||{};
c(b,k);
for(var m=0,l=n.length;
m<l;
++m){n[m](b)
}}}
}()
}else{gadgets.config=window.gadgets["config"];
gadgets.config.register=gadgets.config.register;
gadgets.config.get=gadgets.config.get;
gadgets.config.init=gadgets.config.init;
gadgets.config.update=gadgets.config.update
};;

/* [end] feature=core.config.base */

/* [start] feature=core.log */
gadgets.log=(function(){var e=1;
var a=2;
var f=3;
var c=4;
var d=function(i){b(e,i)
};
gadgets.warn=function(i){b(a,i)
};
gadgets.error=function(i){b(f,i)
};
gadgets.setLogLevel=function(i){h=i
};
function b(j,i){if(j<h||!g){return
}if(j===a&&g.warn){g.warn(i)
}else{if(j===f&&g.error){g.error(i)
}else{if(g.log){g.log(i)
}}}}d.INFO=e;
d.WARNING=a;
d.NONE=c;
var h=e;
var g=window.console?window.console:window.opera?window.opera.postError:undefined;
return d
})();;
;

/* [end] feature=core.log */

/* [start] feature=gapi.util-globals */
gapi.util=window.gapi&&window.gapi.util||{};
;

/* [end] feature=gapi.util-globals */

/* [start] feature=core.config */
(function(){gadgets.config.EnumValidator=function(d){var c=[];
if(arguments.length>1){for(var b=0,a;
(a=arguments[b]);
++b){c.push(a)
}}else{c=d
}return function(f){for(var e=0,g;
(g=c[e]);
++e){if(f===c[e]){return true
}}return false
}
};
gadgets.config.RegExValidator=function(a){return function(b){return a.test(b)
}
};
gadgets.config.ExistsValidator=function(a){return typeof a!=="undefined"
};
gadgets.config.NonEmptyStringValidator=function(a){return typeof a==="string"&&a.length>0
};
gadgets.config.BooleanValidator=function(a){return typeof a==="boolean"
};
gadgets.config.LikeValidator=function(a){return function(c){for(var d in a){if(a.hasOwnProperty(d)){var b=a[d];
if(!b(c[d])){return false
}}}return true
}
}
})();;

/* [end] feature=core.config */

/* [start] feature=core.util.base */
gadgets.util=gadgets.util||{};
(function(){gadgets.util.makeClosure=function(d,f,e){var c=[];
for(var b=2,a=arguments.length;
b<a;
++b){c.push(arguments[b])
}return function(){var g=c.slice();
for(var k=0,h=arguments.length;
k<h;
++k){g.push(arguments[k])
}return f.apply(d,g)
}
};
gadgets.util.makeEnum=function(b){var c,a,d={};
for(c=0;
(a=b[c]);
++c){d[a]=a
}return d
}
})();;

/* [end] feature=core.util.base */

/* [start] feature=core.util.dom */
gadgets.util=gadgets.util||{};
(function(){var c="http://www.w3.org/1999/xhtml";
function b(f,e){var h=e||{};
for(var g in h){if(h.hasOwnProperty(g)){f[g]=h[g]
}}}function d(g,f){var e=["<",g];
var i=f||{};
for(var h in i){if(i.hasOwnProperty(h)){e.push(" ");
e.push(h);
e.push('="');
e.push(gadgets.util.escapeString(i[h]));
e.push('"')
}}e.push("></");
e.push(g);
e.push(">");
return e.join("")
}function a(f){var g="";
if(f.nodeType==3||f.nodeType==4){g=f.nodeValue
}else{if(f.innerText){g=f.innerText
}else{if(f.innerHTML){g=f.innerHTML
}else{if(f.firstChild){var e=[];
for(var h=f.firstChild;
h;
h=h.nextSibling){e.push(a(h))
}g=e.join("")
}}}}return g
}gadgets.util.createElement=function(f){var e;
if((!document.body)||document.body.namespaceURI){try{e=document.createElementNS(c,f)
}catch(g){}}return e||document.createElement(f)
};
gadgets.util.createIframeElement=function(g){var i=gadgets.util.createElement("iframe");
try{var e=d("iframe",g);
var f=gadgets.util.createElement(e);
if(f&&((!i)||((f.tagName==i.tagName)&&(f.namespaceURI==i.namespaceURI)))){i=f
}}catch(h){}b(i,g);
return i
};
gadgets.util.getBodyElement=function(){if(document.body){return document.body
}try{var f=document.getElementsByTagNameNS(c,"body");
if(f&&(f.length==1)){return f[0]
}}catch(e){}return document.documentElement||document
};
gadgets.util.getInnerText=function(e){return a(e)
}
})();;

/* [end] feature=core.util.dom */

/* [start] feature=core.util.event */
gadgets.util=gadgets.util||{};
(function(){gadgets.util.attachBrowserEvent=function(c,b,d,a){if(typeof c.addEventListener!="undefined"){c.addEventListener(b,d,a)
}else{if(typeof c.attachEvent!="undefined"){c.attachEvent("on"+b,d)
}else{gadgets.warn("cannot attachBrowserEvent: "+b)
}}};
gadgets.util.removeBrowserEvent=function(c,b,d,a){if(c.removeEventListener){c.removeEventListener(b,d,a)
}else{if(c.detachEvent){c.detachEvent("on"+b,d)
}else{gadgets.warn("cannot removeBrowserEvent: "+b)
}}}
})();;

/* [end] feature=core.util.event */

/* [start] feature=core.util.onload */
gadgets.util=gadgets.util||{};
(function(){var a=[];
gadgets.util.registerOnLoadHandler=function(b){a.push(b)
};
gadgets.util.runOnLoadHandlers=function(){for(var c=0,b=a.length;
c<b;
++c){a[c]()
}}
})();;

/* [end] feature=core.util.onload */

/* [start] feature=core.util.string */
gadgets.util=gadgets.util||{};
(function(){var a={0:false,10:true,13:true,34:true,39:true,60:true,62:true,92:true,8232:true,8233:true,65282:true,65287:true,65308:true,65310:true,65340:true};
function b(c,d){return String.fromCharCode(d)
}gadgets.util.escape=function(c,g){if(!c){return c
}else{if(typeof c==="string"){return gadgets.util.escapeString(c)
}else{if(typeof c==="array"){for(var f=0,d=c.length;
f<d;
++f){c[f]=gadgets.util.escape(c[f])
}}else{if(typeof c==="object"&&g){var e={};
for(var h in c){if(c.hasOwnProperty(h)){e[gadgets.util.escapeString(h)]=gadgets.util.escape(c[h],true)
}}return e
}}}}return c
};
gadgets.util.escapeString=function(g){if(!g){return g
}var d=[],f,h;
for(var e=0,c=g.length;
e<c;
++e){f=g.charCodeAt(e);
h=a[f];
if(h===true){d.push("&#",f,";")
}else{if(h!==false){d.push(g.charAt(e))
}}}return d.join("")
};
gadgets.util.unescapeString=function(c){if(!c){return c
}return c.replace(/&#([0-9]+);/g,b)
}
})();;

/* [end] feature=core.util.string */

/* [start] feature=core.util.urlparams */
gadgets.util=gadgets.util||{};
(function(){var a=null;
function b(e){var f;
var c=e.indexOf("?");
var d=e.indexOf("#");
if(d===-1){f=e.substr(c+1)
}else{f=[e.substr(c+1,d-c-1),"&",e.substr(d+1)].join("")
}return f.split("&")
}gadgets.util.getUrlParameters=function(p){var d=typeof p==="undefined";
if(a!==null&&d){return a
}var l={};
var f=b(p||document.location.href);
var n=window.decodeURIComponent?decodeURIComponent:unescape;
for(var h=0,g=f.length;
h<g;
++h){var m=f[h].indexOf("=");
if(m===-1){continue
}var c=f[h].substring(0,m);
var o=f[h].substring(m+1);
o=o.replace(/\+/g," ");
try{l[c]=n(o)
}catch(k){}}if(d){a=l
}return l
};
gadgets.util.getUrlParameters()
})();;

/* [end] feature=core.util.urlparams */

/* [start] feature=gapi.util.getOrigin */
gapi.util.getOrigin=function(a){if(!a)return"";a=a.split("#")[0].split("?")[0];a=a.toLowerCase();a.indexOf("//")==0&&(a=window.location.protocol+a);a.indexOf("://")<0&&(a=window.location.protocol+"//"+a);var b=a.substring(a.indexOf("://")+3),c=b.indexOf("/");c!=-1&&(b=b.substring(0,c));var a=a.substring(0,a.indexOf("://")),c="",d=b.indexOf(":");if(d!=-1){var e=b.substring(d+1),b=b.substring(0,d);if(a==="http"&&e!=="80"||a==="https"&&e!=="443")c=":"+e}return a+"://"+b+c};
;

/* [end] feature=gapi.util.getOrigin */

/* [start] feature=core.json */
if(window.JSON&&window.JSON.parse&&window.JSON.stringify){gadgets.json=(function(){var a=/___$/;
function b(d,e){var c=this[d];
return c
}return{parse:function(d){try{return window.JSON.parse(d)
}catch(c){return false
}},stringify:function(d){var h=window.JSON.stringify;
function f(e){return h.call(this,e,b)
}var g=(Array.prototype.toJSON&&h([{x:1}])==='"[{\\"x\\": 1}]"')?f:h;
try{return g(d,function(i,e){return !a.test(i)?e:void 0
})
}catch(c){return null
}}}
})()
};;
;
if(!(window.JSON&&window.JSON.parse&&window.JSON.stringify)){gadgets.json=function(){function f(n){return n<10?"0"+n:n
}Date.prototype.toJSON=function(){return[this.getUTCFullYear(),"-",f(this.getUTCMonth()+1),"-",f(this.getUTCDate()),"T",f(this.getUTCHours()),":",f(this.getUTCMinutes()),":",f(this.getUTCSeconds()),"Z"].join("")
};
var m={"\b":"\\b","\t":"\\t","\n":"\\n","\f":"\\f","\r":"\\r",'"':'\\"',"\\":"\\\\"};
function stringify(value){var a,i,k,l,r=/[\"\\\x00-\x1f\x7f-\x9f]/g,v;
switch(typeof value){case"string":return r.test(value)?'"'+value.replace(r,function(a){var c=m[a];
if(c){return c
}c=a.charCodeAt();
return"\\u00"+Math.floor(c/16).toString(16)+(c%16).toString(16)
})+'"':'"'+value+'"';
case"number":return isFinite(value)?String(value):"null";
case"boolean":case"null":return String(value);
case"object":if(!value){return"null"
}a=[];
if(typeof value.length==="number"&&!value.propertyIsEnumerable("length")){l=value.length;
for(i=0;
i<l;
i+=1){a.push(stringify(value[i])||"null")
}return"["+a.join(",")+"]"
}for(k in value){if(/___$/.test(k)){continue
}if(value.hasOwnProperty(k)){if(typeof k==="string"){v=stringify(value[k]);
if(v){a.push(stringify(k)+":"+v)
}}}}return"{"+a.join(",")+"}"
}return""
}return{stringify:stringify,parse:function(text){if(/^[\],:{}\s]*$/.test(text.replace(/\\["\\\/b-u]/g,"@").replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g,"]").replace(/(?:^|:|,)(?:\s*\[)+/g,""))){return eval("("+text+")")
}return false
}}
}()
};;
gadgets.json.flatten=function(c){var d={};
if(c===null||c===undefined){return d
}for(var a in c){if(c.hasOwnProperty(a)){var b=c[a];
if(null===b||undefined===b){continue
}d[a]=(typeof b==="string")?b:gadgets.json.stringify(b)
}}return d
};;

/* [end] feature=core.json */

/* [start] feature=core.util */
gadgets.util=gadgets.util||{};
(function(){var b={};
var a={};
function c(d){b=d["core.util"]||{}
}if(gadgets.config){gadgets.config.register("core.util",null,c)
}gadgets.util.getFeatureParameters=function(d){return typeof b[d]==="undefined"?null:b[d]
};
gadgets.util.hasFeature=function(d){return typeof b[d]!=="undefined"
};
gadgets.util.getServices=function(){return a
}
})();;

/* [end] feature=core.util */

/* [start] feature=rpc */
gadgets.rpctx=gadgets.rpctx||{};
if(!gadgets.rpctx.wpm){var testSyncPostMessage=false;
gadgets.rpctx.wpm=function(){var e,d;
var c=true;
function b(h,i,g){if(typeof window.addEventListener!="undefined"){window.addEventListener(h,i,g)
}else{if(typeof window.attachEvent!="undefined"){window.attachEvent("on"+h,i)
}}}function a(h,i,g){if(window.removeEventListener){window.removeEventListener(h,i,g)
}else{if(window.detachEvent){window.detachEvent("on"+h,i)
}}}function f(h){var i=gadgets.json.parse(h.data);
if(!i||!i.f){return
}var g=gadgets.rpc.getTargetOrigin(i.f);
if(c&&(typeof h.origin!=="undefined"?h.origin!==g:h.domain!==/^.+:\/\/([^:]+).*/.exec(g)[1])){return
}var g=h.origin;
if(testSyncPostMessage){window.setTimeout(function(){e(i,g)
},0)
}else{e(i,g)
}}return{getCode:function(){return"wpm"
},isParentVerifiable:function(){return true
},init:function(h,i){function g(k){var j=k&&k.rpc||{};
if(String(j.disableForceSecure)==="true"){c=false
}}gadgets.config.register("rpc",null,g);
e=h;
d=i;
b("message",f,false);
d("..",true);
return true
},setup:function(h,g){d(h,true);
return true
},call:function(h,k,j){var g=gadgets.rpc.getTargetOrigin(h);
var i=gadgets.rpc._getTargetWin(h);
if(g){testSyncPostMessage=true;
i.postMessage(gadgets.json.stringify(j),g);
testSyncPostMessage=false
}else{gadgets.error("No relay set (used as window.postMessage targetOrigin), cannot send cross-domain message")
}return true
}}
}()
};;
;
gadgets.rpctx=gadgets.rpctx||{};
if(!gadgets.rpctx.frameElement){gadgets.rpctx.frameElement=function(){var e="__g2c_rpc";
var b="__c2g_rpc";
var d;
var c;
function a(g,k,j){try{if(k!==".."){var f=window.frameElement;
if(typeof f[e]==="function"){if(typeof f[e][b]!=="function"){f[e][b]=function(l){d(gadgets.json.parse(l))
}
}f[e](gadgets.json.stringify(j));
return true
}}else{var i=document.getElementById(g);
if(typeof i[e]==="function"&&typeof i[e][b]==="function"){i[e][b](gadgets.json.stringify(j));
return true
}}}catch(h){}return false
}return{getCode:function(){return"fe"
},isParentVerifiable:function(){return false
},init:function(f,g){d=f;
c=g;
return true
},setup:function(j,f){if(j!==".."){try{var i=document.getElementById(j);
i[e]=function(k){d(gadgets.json.parse(k))
}
}catch(h){return false
}}if(j===".."){c("..",true);
var g=function(){window.setTimeout(function(){gadgets.rpc.call(j,gadgets.rpc.ACK)
},500)
};
gadgets.util.registerOnLoadHandler(g)
}return true
},call:function(f,h,g){return a(f,h,g)
}}
}()
};;
;
;
;
gadgets.rpctx=gadgets.rpctx||{};
if(!gadgets.rpctx.nix){gadgets.rpctx.nix=function(){var c="GRPC____NIXVBS_wrapper";
var d="GRPC____NIXVBS_get_wrapper";
var f="GRPC____NIXVBS_handle_message";
var b="GRPC____NIXVBS_create_channel";
var a=10;
var j=500;
var i={};
var h;
var g=0;
function e(){var l=i[".."];
if(l){return
}if(++g>a){gadgets.warn("Nix transport setup failed, falling back...");
h("..",false);
return
}if(!l&&window.opener&&"GetAuthToken" in window.opener){l=window.opener;
if(l.GetAuthToken()==gadgets.rpc.getAuthToken("..")){var k=gadgets.rpc.getAuthToken("..");
l.CreateChannel(window[d]("..",k),k);
i[".."]=l;
window.opener=null;
h("..",true);
return
}}window.setTimeout(function(){e()
},j)
}return{getCode:function(){return"nix"
},isParentVerifiable:function(){return false
},init:function(l,m){h=m;
if(typeof window[d]!=="unknown"){window[f]=function(o){window.setTimeout(function(){l(gadgets.json.parse(o))
},0)
};
window[b]=function(o,q,p){if(gadgets.rpc.getAuthToken(o)===p){i[o]=q;
h(o,true)
}};
var k="Class "+c+"\n Private m_Intended\nPrivate m_Auth\nPublic Sub SetIntendedName(name)\n If isEmpty(m_Intended) Then\nm_Intended = name\nEnd If\nEnd Sub\nPublic Sub SetAuth(auth)\n If isEmpty(m_Auth) Then\nm_Auth = auth\nEnd If\nEnd Sub\nPublic Sub SendMessage(data)\n "+f+"(data)\nEnd Sub\nPublic Function GetAuthToken()\n GetAuthToken = m_Auth\nEnd Function\nPublic Sub CreateChannel(channel, auth)\n Call "+b+"(m_Intended, channel, auth)\nEnd Sub\nEnd Class\nFunction "+d+"(name, auth)\nDim wrap\nSet wrap = New "+c+"\nwrap.SetIntendedName name\nwrap.SetAuth auth\nSet "+d+" = wrap\nEnd Function";
try{window.execScript(k,"vbscript")
}catch(n){return false
}}return true
},setup:function(o,k){if(o===".."){e();
return true
}try{var m=document.getElementById(o);
var n=window[d](o,k);
m.contentWindow.opener=n
}catch(l){return false
}return true
},call:function(k,n,m){try{if(i[k]){i[k].SendMessage(gadgets.json.stringify(m))
}}catch(l){return false
}return true
}}
}()
};;
;
gadgets.rpctx=gadgets.rpctx||{};
if(!gadgets.rpctx.rmr){gadgets.rpctx.rmr=function(){var h=500;
var f=10;
var i={};
var a=gadgets.util.getUrlParameters()["parent"];
var c;
var j;
function l(q,o,p,n){var r=function(){document.body.appendChild(q);
q.src="about:blank";
if(n){q.onload=function(){m(n)
}
}q.src=o+"#"+p
};
if(document.body){r()
}else{gadgets.util.registerOnLoadHandler(function(){r()
})
}}function d(q){if(typeof i[q]==="object"){return
}var r=document.createElement("iframe");
var o=r.style;
o.position="absolute";
o.top="0px";
o.border="0";
o.opacity="0";
o.width="10px";
o.height="1px";
r.id="rmrtransport-"+q;
r.name=r.id;
var p=gadgets.rpc.getRelayUrl(q);
var n=gadgets.rpc.getOrigin(a);
if(!p){p=n+"/robots.txt"
}i[q]={frame:r,receiveWindow:null,relayUri:p,relayOrigin:n,searchCounter:0,width:10,waiting:true,queue:[],sendId:0,recvId:0,verifySendToken:String(Math.random()),verifyRecvToken:null,originVerified:false};
if(q!==".."){l(r,p,b(q))
}e(q)
}function e(p){var r=null;
i[p].searchCounter++;
try{var o=gadgets.rpc._getTargetWin(p);
if(p===".."){r=o.frames["rmrtransport-"+gadgets.rpc.RPC_ID]
}else{r=o.frames["rmrtransport-.."]
}}catch(q){}var n=false;
if(r){n=g(p,r)
}if(!n){if(i[p].searchCounter>f){return
}window.setTimeout(function(){e(p)
},h)
}}function k(o,q,u,t){var p=null;
if(u!==".."){p=i[".."]
}else{p=i[o]
}if(p){if(q!==gadgets.rpc.ACK){p.queue.push(t)
}if(p.waiting||(p.queue.length===0&&!(q===gadgets.rpc.ACK&&t&&t.ackAlone===true))){return true
}if(p.queue.length>0){p.waiting=true
}var n=p.relayUri+"#"+b(o);
try{p.frame.contentWindow.location=n;
var r=p.width==10?20:10;
p.frame.style.width=r+"px";
p.width=r
}catch(s){return false
}}return true
}function b(o){var p=i[o];
var n={id:p.sendId};
if(p){n.d=Array.prototype.slice.call(p.queue,0);
var q={s:gadgets.rpc.ACK,id:p.recvId};
if(!p.originVerified){q.sendToken=p.verifySendToken
}if(p.verifyRecvToken){q.recvToken=p.verifyRecvToken
}n.d.push(q)
}return gadgets.json.stringify(n)
}function m(y){var v=i[y];
var r=v.receiveWindow.location.hash.substring(1);
var z=gadgets.json.parse(decodeURIComponent(r))||{};
var o=z.d||[];
var p=false;
var u=false;
var w=0;
var n=(v.recvId-z.id);
for(var q=0;
q<o.length;
++q){var t=o[q];
if(t.s===gadgets.rpc.ACK){j(y,true);
v.verifyRecvToken=t.sendToken;
if(!v.originVerified&&t.recvToken&&String(t.recvToken)==String(v.verifySendToken)){v.originVerified=true
}if(v.waiting){u=true
}v.waiting=false;
var s=Math.max(0,t.id-v.sendId);
v.queue.splice(0,s);
v.sendId=Math.max(v.sendId,t.id||0);
continue
}p=true;
if(++w<=n){continue
}++v.recvId;
c(t,v.originVerified?v.relayOrigin:undefined)
}if(p||(u&&v.queue.length>0)){var x=(y==="..")?gadgets.rpc.RPC_ID:"..";
k(y,gadgets.rpc.ACK,x,{ackAlone:p})
}}function g(q,t){var p=i[q];
try{var o=false;
o="document" in t;
if(!o){return false
}o=typeof t.document=="object";
if(!o){return false
}var s=t.location.href;
if(s==="about:blank"){return false
}}catch(n){return false
}p.receiveWindow=t;
function r(){m(q)
}if(typeof t.attachEvent==="undefined"){t.onresize=r
}else{t.attachEvent("onresize",r)
}if(q===".."){l(p.frame,p.relayUri,b(q),q)
}else{m(q)
}return true
}return{getCode:function(){return"rmr"
},isParentVerifiable:function(){return true
},init:function(n,o){c=n;
j=o;
return true
},setup:function(p,n){try{d(p)
}catch(o){gadgets.warn("Caught exception setting up RMR: "+o);
return false
}return true
},call:function(n,p,o){return k(n,o.s,p,o)
}}
}()
};;
;
gadgets.rpctx=gadgets.rpctx||{};
if(!gadgets.rpctx.ifpc){gadgets.rpctx.ifpc=function(){var h=[];
var e=0;
var d;
var a=2000;
var g={};
function c(m){var k=[];
for(var n=0,l=m.length;
n<l;
++n){k.push(encodeURIComponent(gadgets.json.stringify(m[n])))
}return k.join("&")
}function b(m){var k;
for(var j=h.length-1;
j>=0;
--j){var n=h[j];
try{if(n&&(n.recyclable||n.readyState==="complete")){n.parentNode.removeChild(n);
if(window.ActiveXObject){h[j]=n=null;
h.splice(j,1)
}else{n.recyclable=false;
k=n;
break
}}}catch(l){}}if(!k){k=document.createElement("iframe");
k.style.border=k.style.width=k.style.height="0px";
k.style.visibility="hidden";
k.style.position="absolute";
k.onload=function(){this.recyclable=true
};
h.push(k)
}k.src=m;
window.setTimeout(function(){document.body.appendChild(k)
},0)
}function f(j,l){for(var k=l-1;
k>=0;
--k){if(typeof j[k]==="undefined"){return false
}}return true
}return{getCode:function(){return"ifpc"
},isParentVerifiable:function(){return true
},init:function(i,j){d=j;
d("..",true);
return true
},setup:function(j,i){d(j,true);
return true
},call:function(s,r,q){var l=gadgets.rpc.getRelayUrl(s);
++e;
if(!l){gadgets.warn("No relay file assigned for IFPC");
return false
}var i=null,j=[];
if(q.l){var o=q.a;
i=[l,"#",c([r,e,1,0,c([r,q.s,"","",r].concat(o))])].join("");
j.push(i)
}else{i=[l,"#",s,"&",r,"@",e,"&"].join("");
var t=encodeURIComponent(gadgets.json.stringify(q)),n=a-i.length,p=Math.ceil(t.length/n),m=0,k;
while(t.length>0){k=t.substring(0,n);
t=t.substring(n);
j.push([i,p,"&",m,"&",k].join(""));
m+=1
}}do{b(j.shift())
}while(j.length>0);
return true
},_receiveMessage:function(i,n){var o=i[1],m=parseInt(i[2],10),k=parseInt(i[3],10),l=i[i.length-1],j=m===1;
if(m>1){if(!g[o]){g[o]=[]
}g[o][k]=l;
if(f(g[o],m)){l=g[o].join("");
delete g[o];
j=true
}}if(j){n(gadgets.json.parse(decodeURIComponent(l)))
}}}
}()
};;
if(!window.gadgets||!window.gadgets["rpc"]){gadgets.rpc=function(){var M="__cb";
var S="";
var T="__ack";
var f=500;
var G=10;
var b="|";
var u="callback";
var g="origin";
var r="referer";
var s="legacy__";
var q={};
var W={};
var D={};
var B={};
var z=0;
var l={};
var m={};
var d={};
var n={};
var E={};
var e=null;
var p=null;
var A=(window.top!==window.self);
var v=window.name;
var J=function(){};
var P=0;
var Y=1;
var a=2;
var x=window.console;
var V=x&&x.log&&function(ae){x.log(ae)
}||function(){};
var R=(function(){function ae(af){return function(){V(af+": call ignored")
}
}return{getCode:function(){return"noop"
},isParentVerifiable:function(){return true
},init:ae("init"),setup:ae("setup"),call:ae("call")}
})();
if(gadgets.util){d=gadgets.util.getUrlParameters()
}function K(){if(d.rpctx=="flash"){return gadgets.rpctx.flash
}if(d.rpctx=="rmr"){return gadgets.rpctx.rmr
}return typeof window.postMessage==="function"?gadgets.rpctx.wpm:typeof window.postMessage==="object"?gadgets.rpctx.wpm:window.ActiveXObject?(gadgets.rpctx.flash?gadgets.rpctx.flash:gadgets.rpctx.nix):navigator.userAgent.indexOf("WebKit")>0?gadgets.rpctx.rmr:navigator.product==="Gecko"?gadgets.rpctx.frameElement:gadgets.rpctx.ifpc
}function k(aj,ah){if(n[aj]){return
}var af=H;
if(!ah){af=R
}n[aj]=af;
var ae=E[aj]||[];
for(var ag=0;
ag<ae.length;
++ag){var ai=ae[ag];
ai.t=F(aj);
af.call(aj,ai.f,ai)
}E[aj]=[]
}var I=false,U=false;
function N(){if(U){return
}function ae(){I=true
}if(typeof window.addEventListener!="undefined"){window.addEventListener("unload",ae,false)
}else{if(typeof window.attachEvent!="undefined"){window.attachEvent("onunload",ae)
}}U=true
}function j(ae,ai,af,ah,ag){if(!B[ai]||B[ai]!==af){gadgets.error("Invalid auth token. "+B[ai]+" vs "+af);
J(ai,a)
}ag.onunload=function(){if(m[ai]&&!I){J(ai,Y);
gadgets.rpc.removeReceiver(ai)
}};
N();
ah=gadgets.json.parse(decodeURIComponent(ah))
}function Z(ai,af){if(ai&&typeof ai.s==="string"&&typeof ai.f==="string"&&ai.a instanceof Array){if(B[ai.f]){if(B[ai.f]!==ai.t){gadgets.error("Invalid auth token. "+B[ai.f]+" vs "+ai.t);
J(ai.f,a)
}}if(ai.s===T){window.setTimeout(function(){k(ai.f,true)
},0);
return
}if(ai.c){ai[u]=function(aj){var ak=ai.g?s:"";
gadgets.rpc.call(ai.f,ak+M,null,ai.c,aj)
}
}if(af){var ag=t(af);
ai[g]=af;
var ah=ai.r;
if(!ah||t(ah)!=ag){ah=af
}ai[r]=ah
}var ae=(q[ai.s]||q[S]).apply(ai,ai.a);
if(ai.c&&typeof ae!=="undefined"){gadgets.rpc.call(ai.f,M,null,ai.c,ae)
}}}function t(ag){if(!ag){return""
}ag=((ag.split("#"))[0].split("?"))[0];
ag=ag.toLowerCase();
if(ag.indexOf("//")==0){ag=window.location.protocol+ag
}if(ag.indexOf("://")==-1){ag=window.location.protocol+"//"+ag
}var ah=ag.substring(ag.indexOf("://")+3);
var ae=ah.indexOf("/");
if(ae!=-1){ah=ah.substring(0,ae)
}var aj=ag.substring(0,ag.indexOf("://"));
var ai="";
var ak=ah.indexOf(":");
if(ak!=-1){var af=ah.substring(ak+1);
ah=ah.substring(0,ak);
if((aj==="http"&&af!=="80")||(aj==="https"&&af!=="443")){ai=":"+af
}}return aj+"://"+ah+ai
}function C(af,ae){return"/"+af+(ae?b+ae:"")
}function y(ah){if(ah.charAt(0)=="/"){var af=ah.indexOf(b);
var ag=af>0?ah.substring(1,af):ah.substring(1);
var ae=af>0?ah.substring(af+1):null;
return{id:ag,origin:ae}
}else{return null
}}function ad(ag){if(typeof ag==="undefined"||ag===".."){return window.parent
}var af=y(ag);
if(af){return window.top.frames[af.id]
}ag=String(ag);
var ae=window.frames[ag];
if(ae){return ae
}ae=document.getElementById(ag);
if(ae&&ae.contentWindow){return ae.contentWindow
}return null
}function L(ah){var ag=null;
var ae=O(ah);
if(ae){ag=ae
}else{var af=y(ah);
if(af){ag=af.origin
}else{if(ah==".."){ag=d.parent
}else{ag=document.getElementById(ah).src
}}}return t(ag)
}var H=K();
q[S]=function(){V("Unknown RPC service: "+this.s)
};
q[M]=function(af,ae){var ag=l[af];
if(ag){delete l[af];
ag.call(this,ae)
}};
function X(ag,ae){if(m[ag]===true){return
}if(typeof m[ag]==="undefined"){m[ag]=0
}var af=ad(ag);
if(ag===".."||af!=null){if(H.setup(ag,ae)===true){m[ag]=true;
return
}}if(m[ag]!==true&&m[ag]++<G){window.setTimeout(function(){X(ag,ae)
},f)
}else{n[ag]=R;
m[ag]=true
}}function O(af){var ae=W[af];
if(ae&&ae.substring(0,1)==="/"){if(ae.substring(1,2)==="/"){ae=document.location.protocol+ae
}else{ae=document.location.protocol+"//"+document.location.host+ae
}}return ae
}function ac(af,ae,ag){if(ae&&!/http(s)?:\/\/.+/.test(ae)){if(ae.indexOf("//")==0){ae=window.location.protocol+ae
}else{if(ae.charAt(0)=="/"){ae=window.location.protocol+"//"+window.location.host+ae
}else{if(ae.indexOf("://")==-1){ae=window.location.protocol+"//"+ae
}}}}W[af]=ae;
if(typeof ag!=="undefined"){D[af]=!!ag
}}function F(ae){return B[ae]
}function c(ae,af){af=af||"";
B[ae]=String(af);
X(ae,af)
}function ab(af){var ae=af.passReferrer||"";
var ag=ae.split(":",2);
e=ag[0]||"none";
p=ag[1]||"origin"
}function aa(ae){if(Q(ae)){H=gadgets.rpctx.ifpc;
H.init(Z,k)
}}function Q(ae){return String(ae.useLegacyProtocol)==="true"
}function h(af,ae){function ag(aj){var ai=aj&&aj.rpc||{};
ab(ai);
var ah=ai.parentRelayUrl||"";
ah=t(d.parent||ae)+ah;
ac("..",ah,Q(ai));
aa(ai);
c("..",af)
}if(!d.parent&&ae){ag({});
return
}gadgets.config.register("rpc",null,ag)
}function o(af,aj,al){var ai=null;
if(af.charAt(0)!="/"){if(!gadgets.util){return
}ai=document.getElementById(af);
if(!ai){throw new Error("Cannot set up gadgets.rpc receiver with ID: "+af+", element not found.")
}}var ae=ai&&ai.src;
var ag=aj||gadgets.rpc.getOrigin(ae);
ac(af,ag);
var ak=gadgets.util.getUrlParameters(ae);
var ah=al||ak.rpctoken;
c(af,ah)
}function i(ae,ag,ah){if(ae===".."){var af=ah||d.rpctoken||d.ifpctok||"";
h(af,ag)
}else{o(ae,ag,ah)
}}function w(ag){if(e==="bidir"||(e==="c2p"&&ag==="..")||(e==="p2c"&&ag!=="..")){var af=window.location.href;
var ah="?";
if(p==="query"){ah="#"
}else{if(p==="hash"){return af
}}var ae=af.lastIndexOf(ah);
ae=ae===-1?af.length:ae;
return af.substring(0,ae)
}return null
}return{config:function(ae){if(typeof ae.securityCallback==="function"){J=ae.securityCallback
}},register:function(af,ae){if(af===M||af===T){throw new Error("Cannot overwrite callback/ack service")
}if(af===S){throw new Error("Cannot overwrite default service: use registerDefault")
}q[af]=ae
},unregister:function(ae){if(ae===M||ae===T){throw new Error("Cannot delete callback/ack service")
}if(ae===S){throw new Error("Cannot delete default service: use unregisterDefault")
}delete q[ae]
},registerDefault:function(ae){q[S]=ae
},unregisterDefault:function(){delete q[S]
},forceParentVerifiable:function(){if(!H.isParentVerifiable()){H=gadgets.rpctx.ifpc
}},call:function(ae,ag,al,aj){ae=ae||"..";
var ak="..";
if(ae===".."){ak=v
}else{if(ae.charAt(0)=="/"){ak=C(v,gadgets.rpc.getOrigin(window.location.href))
}}++z;
if(al){l[z]=al
}var ai={s:ag,f:ak,c:al?z:0,a:Array.prototype.slice.call(arguments,3),t:B[ae],l:!!D[ae]};
var af=w(ae);
if(af){ai.r=af
}if(ae!==".."&&y(ae)==null&&!document.getElementById(ae)){return
}var ah=n[ae];
if(!ah&&y(ae)!==null){ah=H
}if(ag.indexOf(s)===0){ah=H;
ai.s=ag.substring(s.length);
ai.c=ai.c?ai.c:z
}ai.g=true;
ai.r=ak;
if(!ah){if(!E[ae]){E[ae]=[ai]
}else{E[ae].push(ai)
}return
}if(D[ae]){ah=gadgets.rpctx.ifpc
}if(ah.call(ae,ak,ai)===false){n[ae]=R;
H.call(ae,ak,ai)
}},getRelayUrl:O,setRelayUrl:ac,setAuthToken:c,setupReceiver:i,getAuthToken:F,removeReceiver:function(ae){delete W[ae];
delete D[ae];
delete B[ae];
delete m[ae];
delete n[ae]
},getRelayChannel:function(){return H.getCode()
},receive:function(af,ae){if(af.length>4){H._receiveMessage(af,Z)
}else{j.apply(null,af.concat(ae))
}},receiveSameDomain:function(ae){ae.a=Array.prototype.slice.call(ae.a);
window.setTimeout(function(){Z(ae)
},0)
},getOrigin:t,getTargetOrigin:L,init:function(){if(H.init(Z,k)===false){H=R
}if(A){i("..")
}else{gadgets.config.register("rpc",null,function(af){var ae=af.rpc||{};
ab(ae);
aa(ae)
})
}},_getTargetWin:ad,_parseSiblingId:y,ACK:T,RPC_ID:v||"..",SEC_ERROR_LOAD_TIMEOUT:P,SEC_ERROR_FRAME_PHISH:Y,SEC_ERROR_FORGED_MSG:a}
}();
gadgets.rpc.init()
}else{if(!gadgets.rpc){gadgets.rpc=window.gadgets["rpc"];
gadgets.rpc.config=gadgets.rpc.config;
gadgets.rpc.register=gadgets.rpc.register;
gadgets.rpc.unregister=gadgets.rpc.unregister;
gadgets.rpc.registerDefault=gadgets.rpc.registerDefault;
gadgets.rpc.unregisterDefault=gadgets.rpc.unregisterDefault;
gadgets.rpc.forceParentVerifiable=gadgets.rpc.forceParentVerifiable;
gadgets.rpc.call=gadgets.rpc.call;
gadgets.rpc.getRelayUrl=gadgets.rpc.getRelayUrl;
gadgets.rpc.setRelayUrl=gadgets.rpc.setRelayUrl;
gadgets.rpc.setAuthToken=gadgets.rpc.setAuthToken;
gadgets.rpc.setupReceiver=gadgets.rpc.setupReceiver;
gadgets.rpc.getAuthToken=gadgets.rpc.getAuthToken;
gadgets.rpc.removeReceiver=gadgets.rpc.removeReceiver;
gadgets.rpc.getRelayChannel=gadgets.rpc.getRelayChannel;
gadgets.rpc.receive=gadgets.rpc.receive;
gadgets.rpc.receiveSameDomain=gadgets.rpc.receiveSameDomain;
gadgets.rpc.getOrigin=gadgets.rpc.getOrigin;
gadgets.rpc.getTargetOrigin=gadgets.rpc.getTargetOrigin;
gadgets.rpc._getTargetWin=gadgets.rpc._getTargetWin;
gadgets.rpc._parseSiblingId=gadgets.rpc._parseSiblingId
}};;
;

/* [end] feature=rpc */
gadgets.config.init({"rpc":{"commSwf":"//xpc.googleusercontent.com/gadgets/xpc.swf","passReferrer":"p2c:query","parentRelayUrl":"/rpc_relay.html"}});
(function(){var j=window['___jsl'];if(j['c']&&--j['o']<=0){j['c']();delete j['c'];delete j['o'];}})();var friendconnect_serverBase = "http://www.google.com";var friendconnect_loginUrl = "https://www.google.com/accounts";var friendconnect_gadgetPrefix = "http://www-a-fc-opensocial.googleusercontent.com/gadgets";
var friendconnect_serverVersion = "0.560.7";
var friendconnect_imageUrl = "http://www.google.com/friendconnect/scs/images";
var friendconnect_lightbox = true;
function fca(a){throw a;}var fcb=void 0,fcc=null,fcd=gadgets,fce=Error,fcf=friendconnect_serverBase,fcg=encodeURIComponent,fcaa=parseInt,fch=String,fci=window,fcba=setTimeout,fcca=Object,fcj=document,fck=Array,fcl=Math;function fcda(a,b){return a.length=b}function fcea(a,b){return a.className=b}function fcm(a,b){return a.width=b}function fcn(a,b){return a.innerHTML=b}function fco(a,b){return a.height=b}
var fcp="appendChild",fcq="push",fcfa="toString",fcr="length",fcga="propertyIsEnumerable",fcha="stringify",fc="prototype",fcia="test",fcs="width",fct="round",fcu="slice",fcv="replace",fcw="document",fcja="data",fcx="split",fcy="getElementById",fcz="charAt",fcA="location",fcB="getUrlParameters",fcC="indexOf",fcka="Dialog",fcD="style",fcla="body",fcE="call",fcF="match",fcG="options",fcma="random",fcH="createElement",fcI="json",fcJ="addEventListener",fcK="setAttribute",fcna="href",fcoa="substring",fcL=
"util",fcpa="maxHeight",fcqa="type",fcM="apply",fcra="auth",fcsa="reset",fcta="getSecurityToken",fcua="bind",fcN="name",fcva="display",fcO="height",fcP="register",fcQ="join",fcwa="unshift",fcxa="toLowerCase",fcya="right",goog=goog||{},fcR=this,fcAa=function(a,b,c){a=a[fcx](".");c=c||fcR;!(a[0]in c)&&c.execScript&&c.execScript("var "+a[0]);for(var d;a[fcr]&&(d=a.shift());)!a[fcr]&&fcza(b)?c[d]=b:c=c[d]?c[d]:c[d]={}},fcBa=function(a){var b=typeof a;if(b=="object")if(a){if(a instanceof fck)return"array";
else if(a instanceof fcca)return b;var c=fcca[fc][fcfa][fcE](a);if(c=="[object Window]")return"object";if(c=="[object Array]"||typeof a[fcr]=="number"&&typeof a.splice!="undefined"&&typeof a[fcga]!="undefined"&&!a[fcga]("splice"))return"array";if(c=="[object Function]"||typeof a[fcE]!="undefined"&&typeof a[fcga]!="undefined"&&!a[fcga]("call"))return"function"}else return"null";else if(b=="function"&&typeof a[fcE]=="undefined")return"object";return b},fcza=function(a){return a!==fcb},fcCa=function(a){var b=
fcBa(a);return b=="array"||b=="object"&&typeof a[fcr]=="number"},fcS=function(a){return typeof a=="string"},fcDa=function(a){a=fcBa(a);return a=="object"||a=="array"||a=="function"};fcl.floor(fcl[fcma]()*2147483648)[fcfa](36);
var fcT=function(a){var b=fcBa(a);if(b=="object"||b=="array"){if(a.clone)return a.clone();var b=b=="array"?[]:{},c;for(c in a)b[c]=fcT(a[c]);return b}return a},fcEa=function(a,b,c){return a[fcE][fcM](a[fcua],arguments)},fcFa=function(a,b,c){a||fca(fce());if(arguments[fcr]>2){var d=fck[fc][fcu][fcE](arguments,2);return function(){var c=fck[fc][fcu][fcE](arguments);fck[fc][fcwa][fcM](c,d);return a[fcM](b,c)}}else return function(){return a[fcM](b,arguments)}},fcU=function(a,b,c){fcU=Function[fc][fcua]&&
Function[fc][fcua][fcfa]()[fcC]("native code")!=-1?fcEa:fcFa;return fcU[fcM](fcc,arguments)},fcGa=function(a,b){var c=fck[fc][fcu][fcE](arguments,1);return function(){var b=fck[fc][fcu][fcE](arguments);b[fcwa][fcM](b,c);return a[fcM](this,b)}},fcHa=function(a,b){for(var c in b)a[c]=b[c]},fcIa=Date.now||function(){return+new Date},fcV=function(a,b,c){fcAa(a,b,c)},fcW=function(a,b){function c(){}c.prototype=b[fc];a.Qc=b[fc];a.prototype=new c;a[fc].constructor=a};
Function[fc].bind=Function[fc][fcua]||function(a,b){if(arguments[fcr]>1){var c=fck[fc][fcu][fcE](arguments,1);c[fcwa](this,a);return fcU[fcM](fcc,c)}else return fcU(this,a)};SHA1=function(){this.c=fck(5);this.ca=fck(64);this.Hc=fck(80);this.ta=fck(64);this.ta[0]=128;for(var a=1;a<64;++a)this.ta[a]=0;this[fcsa]()};SHA1[fc].reset=function(){this.c[0]=1732584193;this.c[1]=4023233417;this.c[2]=2562383102;this.c[3]=271733878;this.c[4]=3285377520;this.Aa=this.A=0};SHA1[fc].ya=function(a,b){return(a<<b|a>>>32-b)&4294967295};
SHA1[fc].L=function(a){for(var b=this.Hc,c=0;c<64;c+=4){var d=a[c]<<24|a[c+1]<<16|a[c+2]<<8|a[c+3];b[c/4]=d}for(c=16;c<80;++c)b[c]=this.ya(b[c-3]^b[c-8]^b[c-14]^b[c-16],1);for(var a=this.c[0],d=this.c[1],e=this.c[2],g=this.c[3],j=this.c[4],i,k,c=0;c<80;++c)c<40?c<20?(i=g^d&(e^g),k=1518500249):(i=d^e^g,k=1859775393):c<60?(i=d&e|g&(d|e),k=2400959708):(i=d^e^g,k=3395469782),i=this.ya(a,5)+i+j+k+b[c]&4294967295,j=g,g=e,e=this.ya(d,30),d=a,a=i;this.c[0]=this.c[0]+a&4294967295;this.c[1]=this.c[1]+d&4294967295;
this.c[2]=this.c[2]+e&4294967295;this.c[3]=this.c[3]+g&4294967295;this.c[4]=this.c[4]+j&4294967295};SHA1[fc].update=function(a,b){b||(b=a[fcr]);var c=0;if(this.A==0)for(;c+64<b;)this.L(a[fcu](c,c+64)),c+=64,this.Aa+=64;for(;c<b;)if(this.ca[this.A++]=a[c++],++this.Aa,this.A==64){this.A=0;for(this.L(this.ca);c+64<b;)this.L(a[fcu](c,c+64)),c+=64,this.Aa+=64}};
SHA1[fc].digest=function(){var a=fck(20),b=this.Aa*8;this.A<56?this.update(this.ta,56-this.A):this.update(this.ta,64-(this.A-56));for(var c=63;c>=56;--c)this.ca[c]=b&255,b>>>=8;this.L(this.ca);for(c=b=0;c<5;++c)for(var d=24;d>=0;d-=8)a[b++]=this.c[c]>>d&255;return a};G_HMAC=function(a,b,c){(!a||typeof a!="object"||!a[fcsa]||!a.update||!a.digest)&&fca(fce("Invalid hasher object. Hasher unspecified or does not implement expected interface."));b.constructor!=fck&&fca(fce("Invalid key."));c&&typeof c!="number"&&fca(fce("Invalid block size."));this.k=a;this.ba=c||16;this.Wb=fck(this.ba);this.Vb=fck(this.ba);b[fcr]>this.ba&&(this.k.update(b),b=this.k.digest());for(c=0;c<this.ba;c++)a=c<b[fcr]?b[c]:0,this.Wb[c]=a^G_HMAC.OPAD,this.Vb[c]=a^G_HMAC.IPAD};G_HMAC.OPAD=92;
G_HMAC.IPAD=54;G_HMAC[fc].reset=function(){this.k[fcsa]();this.k.update(this.Vb)};G_HMAC[fc].update=function(a){a.constructor!=fck&&fca(fce("Invalid data. Data must be an array."));this.k.update(a)};G_HMAC[fc].digest=function(){var a=this.k.digest();this.k[fcsa]();this.k.update(this.Wb);this.k.update(a);return this.k.digest()};G_HMAC[fc].Ib=function(a){this[fcsa]();this.update(a);return this.digest()};var fcJa=function(a,b){for(var c=1;c<arguments[fcr];c++)var d=fch(arguments[c])[fcv](/\$/g,"$$$$"),a=a[fcv](/\%s/,d);return a},fcKa=function(a,b){var c=fch(a)[fcxa](),d=fch(b)[fcxa]();return c<d?-1:c==d?0:1},fcQa=function(a,b){if(b)return a[fcv](fcLa,"&amp;")[fcv](fcMa,"&lt;")[fcv](fcNa,"&gt;")[fcv](fcOa,"&quot;");else{if(!fcPa[fcia](a))return a;a[fcC]("&")!=-1&&(a=a[fcv](fcLa,"&amp;"));a[fcC]("<")!=-1&&(a=a[fcv](fcMa,"&lt;"));a[fcC](">")!=-1&&(a=a[fcv](fcNa,"&gt;"));a[fcC]('"')!=-1&&(a=a[fcv](fcOa,
"&quot;"));return a}},fcLa=/&/g,fcMa=/</g,fcNa=/>/g,fcOa=/\"/g,fcPa=/[&<>\"]/,fcSa=function(a,b){for(var c=0,d=fch(a)[fcv](/^[\s\xa0]+|[\s\xa0]+$/g,"")[fcx]("."),e=fch(b)[fcv](/^[\s\xa0]+|[\s\xa0]+$/g,"")[fcx]("."),g=fcl.max(d[fcr],e[fcr]),j=0;c==0&&j<g;j++){var i=d[j]||"",k=e[j]||"",l=RegExp("(\\d*)(\\D*)","g"),h=RegExp("(\\d*)(\\D*)","g");do{var f=l.exec(i)||["","",""],m=h.exec(k)||["","",""];if(f[0][fcr]==0&&m[0][fcr]==0)break;var c=f[1][fcr]==0?0:fcaa(f[1],10),p=m[1][fcr]==0?0:fcaa(m[1],10),c=
fcRa(c,p)||fcRa(f[2][fcr]==0,m[2][fcr]==0)||fcRa(f[2],m[2])}while(c==0)}return c},fcRa=function(a,b){if(a<b)return-1;else if(a>b)return 1;return 0},fcTa={},fcUa=function(a){return fcTa[a]||(fcTa[a]=fch(a)[fcv](/\-([a-z])/g,function(a,c){return c.toUpperCase()}))};var fcVa,fcWa,fcXa,fcYa,fcZa,fc_a=function(){return fcR.navigator?fcR.navigator.userAgent:fcc},fc0a=function(){return fcR.navigator},fc1a=function(){fcZa=fcYa=fcXa=fcWa=fcVa=!1;var a;if(a=fc_a()){var b=fc0a();fcVa=a[fcC]("Opera")==0;fcWa=!fcVa&&a[fcC]("MSIE")!=-1;fcYa=(fcXa=!fcVa&&a[fcC]("WebKit")!=-1)&&a[fcC]("Mobile")!=-1;fcZa=!fcVa&&!fcXa&&b.product=="Gecko"}};fc1a();
var fc2a=fcVa,fcX=fcWa,fc3a=fcZa,fc4a=fcXa,fc5a=fcYa,fc6a=function(){var a=fc0a();return a&&a.platform||""},fc7a=fc6a(),fc8a=function(){fc7a[fcC]("Mac");fc7a[fcC]("Win");fc7a[fcC]("Linux");fc0a()&&(fc0a().appVersion||"")[fcC]("X11")};fc8a();
var fc$a=function(){var a="",b;fc2a&&fcR.opera?(a=fcR.opera.version,a=typeof a=="function"?a():a):(fc3a?b=/rv\:([^\);]+)(\)|;)/:fcX?b=/MSIE\s+([^\);]+)(\)|;)/:fc4a&&(b=/WebKit\/(\S+)/),b&&(a=(a=b.exec(fc_a()))?a[1]:""));return fcX&&(b=fc9a(),b>parseFloat(a))?fch(b):a},fc9a=function(){var a=fcR[fcw];return a?a.documentMode:fcb},fcab=fc$a(),fcbb={},fccb=function(a){return fcbb[a]||(fcbb[a]=fcSa(fcab,a)>=0)},fcdb={},fceb=function(a){return fcdb[a]||(fcdb[a]=fcX&&fcj.documentMode&&fcj.documentMode>=a)};var fcfb=function(a){this.stack=fce().stack||"";if(a)this.message=fch(a)};fcW(fcfb,fce);fcfb[fc].name="CustomError";var fcgb=function(a,b){b[fcwa](a);fcfb[fcE](this,fcJa[fcM](fcc,b));b.shift();this.messagePattern=a};fcW(fcgb,fcfb);fcgb[fc].name="AssertionError";var fchb=function(a,b,c,d){var e="Assertion failed";if(c){e+=": "+c;var g=d}else a&&(e+=": "+a,g=b);fca(new fcgb(""+e,g||[]))},fcib=function(a,b,c){a||fchb("",fcc,b,fck[fc][fcu][fcE](arguments,2));return a};var fcY=fck[fc],fcjb=fcY[fcC]?function(a,b,c){fcib(a[fcr]!=fcc);return fcY[fcC][fcE](a,b,c)}:function(a,b,c){c=c==fcc?0:c<0?fcl.max(0,a[fcr]+c):c;if(fcS(a))return!fcS(b)||b[fcr]!=1?-1:a[fcC](b,c);for(;c<a[fcr];c++)if(c in a&&a[c]===b)return c;return-1},fckb=fcY.forEach?function(a,b,c){fcib(a[fcr]!=fcc);fcY.forEach[fcE](a,b,c)}:function(a,b,c){for(var d=a[fcr],e=fcS(a)?a[fcx](""):a,g=0;g<d;g++)g in e&&b[fcE](c,e[g],g,a)},fclb=function(a,b){return fcjb(a,b)>=0},fcmb=function(a){return fcY.concat[fcM](fcY,
arguments)},fcnb=function(a){if(fcBa(a)=="array")return fcmb(a);else{for(var b=[],c=0,d=a[fcr];c<d;c++)b[c]=a[c];return b}},fcob=function(a,b,c){fcib(a[fcr]!=fcc);return arguments[fcr]<=2?fcY[fcu][fcE](a,b):fcY[fcu][fcE](a,b,c)};var fcpb=function(a){for(var b=[],c=0,d=0;d<a[fcr];d++){for(var e=a.charCodeAt(d);e>255;)b[c++]=e&255,e>>=8;b[c++]=e}return b};var fcqb=fcc,fcrb=fcc,fcsb=fcc,fctb=fcc,fcvb=function(a,b){fcCa(a)||fca(fce("encodeByteArray takes an array as a parameter"));fcub();for(var c=b?fcsb:fcqb,d=[],e=0;e<a[fcr];e+=3){var g=a[e],j=e+1<a[fcr],i=j?a[e+1]:0,k=e+2<a[fcr],l=k?a[e+2]:0,h=g>>2,g=(g&3)<<4|i>>4,i=(i&15)<<2|l>>6;l&=63;k||(l=64,j||(i=64));d[fcq](c[h],c[g],c[i],c[l])}return d[fcQ]("")},fcwb=function(a,b){fcub();for(var c=b?fctb:fcrb,d=[],e=0;e<a[fcr];){var g=c[a[fcz](e++)],j=e<a[fcr],j=j?c[a[fcz](e)]:0;++e;var i=e<a[fcr],i=i?c[a[fcz](e)]:
0;++e;var k=e<a[fcr],k=k?c[a[fcz](e)]:0;++e;(g==fcc||j==fcc||i==fcc||k==fcc)&&fca(fce());g=g<<2|j>>4;d[fcq](g);i!=64&&(g=j<<4&240|i>>2,d[fcq](g),k!=64&&(g=i<<6&192|k,d[fcq](g)))}return d},fcub=function(){if(!fcqb){fcqb={};fcrb={};fcsb={};fctb={};for(var a=0;a<65;a++)fcqb[a]="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="[fcz](a),fcrb[fcqb[a]]=a,fcsb[a]="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_."[fcz](a),fctb[fcsb[a]]=a}};var fcZ=function(a){this.wb=a},fcxb=/\s*;\s*/;fcZ[fc].Tb=function(a){return!/[;=\s]/[fcia](a)};fcZ[fc].Ub=function(a){return!/[;\r\n]/[fcia](a)};fcZ[fc].set=function(a,b,c,d,e,g){this.Tb(a)||fca(fce('Invalid cookie name "'+a+'"'));this.Ub(b)||fca(fce('Invalid cookie value "'+b+'"'));fcza(c)||(c=-1);e=e?";domain="+e:"";d=d?";path="+d:"";g=g?";secure":"";c<0?c="":(c=c==0?new Date(1970,1,1):new Date(fcIa()+c*1E3),c=";expires="+c.toUTCString());this.vc(a+"="+b+e+d+c+g)};
fcZ[fc].get=function(a,b){for(var c=a+"=",d=this.ka(),e=0,g;g=d[e];e++)if(g[fcC](c)==0)return g.substr(c[fcr]);return b};fcZ[fc].remove=function(a,b,c){var d=this.M(a);this.set(a,"",0,b,c);return d};fcZ[fc].z=function(){return this.ia().keys};fcZ[fc].F=function(){return this.ia().values};fcZ[fc].P=function(){var a=this.Oa();return!a?0:this.ka()[fcr]};fcZ[fc].M=function(a){return fcza(this.get(a))};fcZ[fc].clear=function(){for(var a=this.ia().keys,b=a[fcr]-1;b>=0;b--)this.remove(a[b])};
fcZ[fc].vc=function(a){this.wb.cookie=a};fcZ[fc].Oa=function(){return this.wb.cookie};fcZ[fc].ka=function(){return(this.Oa()||"")[fcx](fcxb)};fcZ[fc].ia=function(){for(var a=this.ka(),b=[],c=[],d,e,g=0;e=a[g];g++)d=e[fcC]("="),d==-1?(b[fcq](""),c[fcq](e)):(b[fcq](e[fcoa](0,d)),c[fcq](e[fcoa](d+1)));return{keys:b,values:c}};var fcyb=new fcZ(fcj);fcyb.MAX_COOKIE_LENGTH=3950;var fc_=function(a,b){fcm(this,a);fco(this,b)};fc_[fc].clone=function(){return new fc_(this[fcs],this[fcO])};fc_[fc].toString=function(){return"("+this[fcs]+" x "+this[fcO]+")"};fc_[fc].ceil=function(){fcm(this,fcl.ceil(this[fcs]));fco(this,fcl.ceil(this[fcO]));return this};fc_[fc].floor=function(){fcm(this,fcl.floor(this[fcs]));fco(this,fcl.floor(this[fcO]));return this};fc_[fc].round=function(){fcm(this,fcl[fct](this[fcs]));fco(this,fcl[fct](this[fcO]));return this};
fc_[fc].scale=function(a){this.width*=a;this.height*=a;return this};var fczb=function(a,b,c){for(var d in a)b[fcE](c,a[d],d,a)},fcAb=function(a){var b=[],c=0,d;for(d in a)b[c++]=a[d];return b},fcBb=function(a){var b=[],c=0,d;for(d in a)b[c++]=d;return b},fcCb=["constructor","hasOwnProperty","isPrototypeOf","propertyIsEnumerable","toLocaleString","toString","valueOf"],fcDb=function(a,b){for(var c,d,e=1;e<arguments[fcr];e++){d=arguments[e];for(c in d)a[c]=d[c];for(var g=0;g<fcCb[fcr];g++)c=fcCb[g],fcca[fc].hasOwnProperty[fcE](d,c)&&(a[c]=d[c])}};var fcEb=!fcX||fceb(9);!fc3a&&!fcX||fcX&&fceb(9)||fc3a&&fccb("1.9.1");fcX&&fccb("9");var fcFb=function(a){return(a=a.className)&&typeof a[fcx]=="function"?a[fcx](/\s+/):[]},fcHb=function(a,b){var c=fcFb(a),d=fcob(arguments,1),d=fcGb(c,d);fcea(a,c[fcQ](" "));return d},fcGb=function(a,b){for(var c=0,d=0;d<b[fcr];d++)fclb(a,b[d])||(a[fcq](b[d]),c++);return c==b[fcr]};var fcIb=function(a){return fcS(a)?fcj[fcy](a):a},fcJb=fcIb,fcKb=function(a,b,c,d){a=d||a;b=b&&b!="*"?b.toUpperCase():"";if(a.querySelectorAll&&a.querySelector&&(!fc4a||fcj.compatMode=="CSS1Compat"||fccb("528"))&&(b||c))return c=b+(c?"."+c:""),a.querySelectorAll(c);if(c&&a.getElementsByClassName)if(a=a.getElementsByClassName(c),b){for(var d={},e=0,g=0,j;j=a[g];g++)b==j.nodeName&&(d[e++]=j);fcda(d,e);return d}else return a;a=a.getElementsByTagName(b||"*");if(c){d={};for(g=e=0;j=a[g];g++)b=j.className,
typeof b[fcx]=="function"&&fclb(b[fcx](/\s+/),c)&&(d[e++]=j);fcda(d,e);return d}else return a},fcMb=function(a,b){fczb(b,function(b,d){d=="style"?a[fcD].cssText=b:d=="class"?fcea(a,b):d=="for"?a.htmlFor=b:d in fcLb?a[fcK](fcLb[d],b):a[d]=b})},fcLb={cellpadding:"cellPadding",cellspacing:"cellSpacing",colspan:"colSpan",rowspan:"rowSpan",valign:"vAlign",height:"height",width:"width",usemap:"useMap",frameborder:"frameBorder",maxlength:"maxLength",type:"type"},fcNb=function(a){var b=a[fcw];if(fc4a&&!fccb("500")&&
!fc5a){typeof a.innerHeight=="undefined"&&(a=fci);var b=a.innerHeight,c=a[fcw].documentElement.scrollHeight;a==a.top&&c<b&&(b-=15);return new fc_(a.innerWidth,b)}a=b.compatMode=="CSS1Compat"?b.documentElement:b[fcla];return new fc_(a.clientWidth,a.clientHeight)},fc0=function(a,b,c){return fcOb(fcj,arguments)},fcOb=function(a,b){var c=b[0],d=b[1];if(!fcEb&&d&&(d[fcN]||d[fcqa])){c=["<",c];d[fcN]&&c[fcq](' name="',fcQa(d[fcN]),'"');if(d[fcqa]){c[fcq](' type="',fcQa(d[fcqa]),'"');var e={};fcDb(e,d);d=
e;delete d[fcqa]}c[fcq](">");c=c[fcQ]("")}c=a[fcH](c);d&&(fcS(d)?fcea(c,d):fcBa(d)=="array"?fcHb[fcM](fcc,[c].concat(d)):fcMb(c,d));b[fcr]>2&&fcPb(a,c,b,2);return c},fcPb=function(a,b,c,d){function e(c){c&&b[fcp](fcS(c)?a.createTextNode(c):c)}for(;d<c[fcr];d++){var g=c[d];fcCa(g)&&!(fcDa(g)&&g.nodeType>0)?fckb(fcQb(g)?fcnb(g):g,e):e(g)}},fcQb=function(a){if(a&&typeof a[fcr]=="number")if(fcDa(a))return typeof a.item=="function"||typeof a.item=="string";else if(fcBa(a)=="function")return typeof a.item==
"function";return!1};var fcRb="StopIteration"in fcR?fcR.StopIteration:fce("StopIteration"),fcSb=function(){};fcSb[fc].next=function(){fca(fcRb)};fcSb[fc].__iterator__=function(){return this};var fc1=function(a,b){this.i={};this.e=[];var c=arguments[fcr];if(c>1){c%2&&fca(fce("Uneven number of arguments"));for(var d=0;d<c;d+=2)this.set(arguments[d],arguments[d+1])}else a&&this.nb(a)};fc1[fc].q=0;fc1[fc].J=0;fc1[fc].P=function(){return this.q};fc1[fc].F=function(){this.K();for(var a=[],b=0;b<this.e[fcr];b++){var c=this.e[b];a[fcq](this.i[c])}return a};fc1[fc].z=function(){this.K();return this.e.concat()};fc1[fc].M=function(a){return fcTb(this.i,a)};
fc1[fc].clear=function(){this.i={};fcda(this.e,0);this.J=this.q=0};fc1[fc].remove=function(a){return fcTb(this.i,a)?(delete this.i[a],this.q--,this.J++,this.e[fcr]>2*this.q&&this.K(),!0):!1};fc1[fc].K=function(){if(this.q!=this.e[fcr]){for(var a=0,b=0;a<this.e[fcr];){var c=this.e[a];fcTb(this.i,c)&&(this.e[b++]=c);a++}fcda(this.e,b)}if(this.q!=this.e[fcr]){for(var d={},b=a=0;a<this.e[fcr];)c=this.e[a],fcTb(d,c)||(this.e[b++]=c,d[c]=1),a++;fcda(this.e,b)}};
fc1[fc].get=function(a,b){return fcTb(this.i,a)?this.i[a]:b};fc1[fc].set=function(a,b){fcTb(this.i,a)||(this.q++,this.e[fcq](a),this.J++);this.i[a]=b};fc1[fc].nb=function(a){var b;a instanceof fc1?(b=a.z(),a=a.F()):(b=fcBb(a),a=fcAb(a));for(var c=0;c<b[fcr];c++)this.set(b[c],a[c])};fc1[fc].clone=function(){return new fc1(this)};
fc1[fc].__iterator__=function(a){this.K();var b=0,c=this.e,d=this.i,e=this.J,g=this,j=new fcSb;j.next=function(){for(;;){e!=g.J&&fca(fce("The map has changed since the iterator was created"));b>=c[fcr]&&fca(fcRb);var j=c[b++];return a?j:d[j]}};return j};var fcTb=function(a,b){return fcca[fc].hasOwnProperty[fcE](a,b)};var fcVb=function(a,b,c){fcS(b)?fcUb(a,c,b):fczb(b,fcGa(fcUb,a))},fcUb=function(a,b,c){a[fcD][fcUa(c)]=b},fcWb=function(a,b){var c=a.nodeType==9?a:a.ownerDocument||a[fcw];return c.defaultView&&c.defaultView.getComputedStyle&&(c=c.defaultView.getComputedStyle(a,fcc))?c[b]||c.getPropertyValue(b):""},fcZb=function(a,b,c){b instanceof fc_?(c=b[fcO],b=b[fcs]):c==fcb&&fca(fce("missing height argument"));fcXb(a,b);fcYb(a,c)},fc_b=function(a,b){typeof a=="number"&&(a=(b?fcl[fct](a):a)+"px");return a},fcYb=
function(a,b){fco(a[fcD],fc_b(b,!0))},fcXb=function(a,b){fcm(a[fcD],fc_b(b,!0))},fc0b=function(a){if((fcWb(a,"display")||(a.currentStyle?a.currentStyle[fcva]:fcc)||a[fcD][fcva])!="none")return new fc_(a.offsetWidth,a.offsetHeight);var b=a[fcD],c=b[fcva],d=b.visibility,e=b.position;b.visibility="hidden";b.position="absolute";b.display="inline";var g=a.offsetWidth,a=a.offsetHeight;b.display=c;b.position=e;b.visibility=d;return new fc_(g,a)},fc1b=function(a,b){a[fcD].display=b?"":"none"};var fc2b={},fc3b={};var fc4b=function(a,b,c,d){b=b||"800";c=c||"550";d=d||"friendconnect";a=fci.open(a,d,"menubar=no,toolbar=no,dialog=yes,location=yes,alwaysRaised=yes,width="+b+",height="+c+",resizable=yes,scrollbars=1,status=1");fci.focus&&a&&a.focus()},fc5b=function(a,b){var c=fcd[fcL][fcB]().communityId;fcd.rpc[fcE](fcc,"signin",fcc,c,a,b)};fcV("goog.peoplesense.util.openPopup",fc4b);fcV("goog.peoplesense.util.finishSignIn",fc5b);var fc8b=function(a,b){var c=fc6b()+"/friendconnect/invite/friends",d=fcg(shindig[fcra][fcta]());fc7b(c,d,a,b)},fc7b=function(a,b,c,d){a+="?st="+b;c&&(a+="&customMessage="+fcg(c));d&&(a+="&customInviteUrl="+fcg(d));b=760;fcX&&(b+=25);fc4b(a,fch(b),"515","friendconnect_invite")};fcV("goog.peoplesense.util.invite",fc8b);fcV("goog.peoplesense.util.inviteFriends",fc7b);var fc9b=function(a){this.url=a};fc9b[fc].l=function(a,b){(this.url[fcC]("?"+a+"=")>=0||this.url[fcC]("&"+a+"=")>=0)&&fca(fce("duplicate: "+a));if(b===fcc||b===fcb)return this;var c=this.url[fcC]("?")>=0?"&":"?";this.url+=c+a+"="+fcg(b);return this};fc9b[fc].toString=function(){return this.url};var fc6b=function(){return fci.friendconnect_serverBase},fc$b=function(a,b,c,d,e,g,j){b=b||"800";c=c||"550";d=d||"friendconnect";g=g||!1;fcd.rpc[fcE](fcc,"openLightboxIframe",j,a,shindig[fcra][fcta](),b,c,d,e,fcc,fcc,fcc,g)},fcac=function(a,b){var c=fcd[fcL][fcB]().psinvite||"",d=new fc9b(fc6b()+"/friendconnect/signin/home");d.l("st",fci.shindig[fcra][fcta]());d.l("psinvite",c);d.l("iframeId",a);d.l("loginProvider",b);d.l("subscribeOnSignin","1");fc4b(d[fcfa]());return!1},fcbc=function(){var a=fcd[fcL][fcB]().communityId;
fcd.rpc[fcE](fcc,"signout",fcc,a)},fcdc=function(a,b){var c=fc6b()+"/friendconnect/settings/edit?st="+fcg(shindig[fcra][fcta]())+(a?"&iframeId="+fcg(a):"");b&&(c=c+"&"+b);fccc(c)},fcec=function(a){a=fc6b()+"/friendconnect/settings/siteProfile?st="+fcg(a);fccc(a)},fccc=function(a){var b=800,c=510;fcX&&(b+=25);fc4b(a,fch(b),fch(c))},fcfc=function(a,b,c,d){var d=d||2,e=fcc;if(b=="text")e=fc0("div",{"class":"gfc-button-text"},fc0("div",{"class":"gfc-icon"},fc0("a",{href:"javascript:void(0);"},c))),a[fcp](e);
else if(b=="long"||b=="standard")e=d==1?fc0("div",{"class":"gfc-inline-block gfc-primaryactionbutton gfc-button-base"},fc0("div",{"class":"gfc-inline-block gfc-button-base-outer-box"},fc0("div",{"class":"gfc-inline-block gfc-button-base-inner-box"},fc0("div",{"class":"gfc-button-base-pos"},fc0("div",{"class":"gfc-button-base-top-shadow",innerHTML:"&nbsp;"}),fc0("div",{"class":"gfc-button-base-content"},fc0("div",{"class":"gfc-icon"},c)))))):fc0("table",{"class":"gfc-button-base-v2 gfc-button",cellpadding:"0",
cellspacing:"0"},fc0("tbody",{"class":""},fc0("tr",{"class":""},fc0("td",{"class":"gfc-button-base-v2 gfc-button-1"}),fc0("td",{"class":"gfc-button-base-v2 gfc-button-2"},c),fc0("td",{"class":"gfc-button-base-v2 gfc-button-3"})))),a[fcp](e),b=="standard"&&(b=fc0("div",{"class":"gfc-footer-msg"},"with Google Friend Connect"),d==1&&a[fcp](fc0("br")),a[fcp](b));return e},fcgc=function(a,b){a||fca("google.friendconnect.renderSignInButton: missing options");var c=a[fcD]||"standard",d=a.text,e=a.version;
if(c=="standard")d=a.text||"Sign in";else if(c=="text"||c=="long")d=a.text||"Sign in with Friend Connect";var g=a.element;if(!g)(g=a.id)||fca("google.friendconnect.renderSignInButton: options[id] and options[element] == null"),(g=fcJb(g))||fca("google.friendconnect.renderSignInButton: element "+a.id+" not found");fcn(g,"");c=fcfc(g,c,d,e);fci[fcJ]?c[fcJ]("click",b,!1):c.attachEvent("onclick",b)},fchc=function(a,b){b=b||fcU(fcac,fcc,fcc,fcc,fcc);fcgc(a,b)},fcic=function(a,b){fcd.rpc[fcE](fcc,"putReloadViewParam",
fcc,a,b);var c=fcd.views.getParams();c[a]=b},fcjc=function(a,b){var c=new fc9b("/friendconnect/gadgetshare/friends");c.l("customMessage",a);c.l("customInviteUrl",b);c.l("container","glb");var d=310;fcX&&(d+=25);fc$b(c[fcfa](),fch(d),"370")};fcV("goog.peoplesense.util.getBaseUrl",fc6b);fcV("goog.peoplesense.util.finishSignIn",fc5b);fcV("goog.peoplesense.util.signout",fcbc);fcV("goog.peoplesense.util.signin",fcac);fcV("goog.peoplesense.util.editSettings",fcdc);
fcV("goog.peoplesense.util.editSSProfile",fcec);fcV("goog.peoplesense.util.setStickyViewParamToken",fcic);fcV("google.friendconnect.renderSignInButton",fchc);fcV("goog.peoplesense.util.share",fcjc);fcV("goog.peoplesense.util.userAgent.IE",fcX);var fckc={},fclc={},fc2=function(a){this.h=new fc1;this.snippetId=a.id;this.site=a.site;var a=a["view-params"],b=a.skin;this.gc=(b?b.POSITION:"top")||"top";this.Ic={allowAnonymousPost:a.allowAnonymousPost||!1,scope:a.scope||"SITE",docId:a.docId||"",features:a.features||"video,comment",startMaximized:"true",disableMinMax:"true",skin:b};this.absoluteBottom=fcX&&!fccb("7");this.fixedIeSizes=fcX;fci[fcJ]?fci[fcJ]("resize",fcU(this.db,this),!1):fci.attachEvent("onresize",fcU(this.db,this));this.sb()};
fc2[fc].sb=function(){this.site||fca(fce("Must supply site ID."));this.snippetId||fca(fce("Must supply a snippet ID."))};fc2[fc].b=10;fc2[fc].Da=1;fc2[fc].p="fc-friendbar-";fc2[fc].t=fc2[fc].p+"outer";fc2[fc].gb=fc2[fc].t+"-shadow";fc2[fc].render=function(){fcj.write(this.Ab());var a=fcIb(this.snippetId);fcn(a,this.O())};fc2[fc].Bb=function(){var a=fcIb(this.t);return a=fc0b(a)[fcs]};fc2[fc].db=function(){for(var a=this.h.z(),b=0;b<a[fcr];b++)this.sc(a[b]);goog&&fc2b&&fc3b&&fcmc&&fcnc("resize")};
fc2[fc].n=function(){return this.gc};fc2[fc].d=function(a){return this.p+"shadow-"+a};fc2[fc].ja=function(a){return this.p+"menus-"+a};fc2[fc].R=function(a){return this.p+a+"Target"};fc2[fc].ga=function(a){return this.p+a+"Drawer"};fc2[fc].Ta=function(){return this.R("")};fc2[fc].Ua=function(){return this.p+"wallpaper"};fc2[fc].Pa=function(){return this.ga("")};
fc2[fc].Ab=function(){var a=fci.friendconnect_imageUrl+"/",b=a+"shadow_tc.png",c=a+"shadow_bc.png",d=a+"shadow_bl.png",e=a+"shadow_tl.png",g=a+"shadow_tr.png",j=a+"shadow_br.png";a+="shadow_cr.png";var i=function(a,b){return fcX?'filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src="'+a+'", sizingMethod="scale");':"background-image: url("+a+");background-repeat: "+b+"; "},k="position:absolute; top:";this.n()!="top"&&(k="position:fixed; bottom:",this.absoluteBottom&&(k="position:absolute; bottom:"));
var l=c;this.n()!="top"&&(l=b);var h=0,f=[];f[h++]='<style type="text/css">';this.n()!="top"&&this.absoluteBottom&&(f[h++]="html, body {height: 100%; overflow: auto; };");f[h++]="#"+this.t+" {";f[h++]="background:#E0ECFF;";f[h++]="left:0;";f[h++]="height: "+(fcX?"35px;":"36px;");this.n()!="top"&&this.absoluteBottom&&(f[h++]="margin-right: 20px;");f[h++]="padding:0;";f[h++]=k+" 0;";f[h++]="width:100%;";f[h++]="z-index:5000;";f[h++]="}";f[h++]="#"+this.gb+" {";f[h++]=i(l,"repeat-x");f[h++]="left:0;";
f[h++]="height:"+this.b+"px;";this.n()!="top"&&this.absoluteBottom&&(f[h++]="margin-right: 20px;");f[h++]="padding:0;";f[h++]=k+(fcX?"35px;":"36px;");f[h++]="width:100%;";f[h++]="z-index:4998;";f[h++]="}";f[h++]="."+this.Pa()+" {";f[h++]="display: block;";f[h++]="padding:0;";f[h++]=k+(fcX?"34px;":"35px;");f[h++]="z-index:4999;";f[h++]="}";f[h++]="."+this.Ua()+" {";f[h++]="background: white;";f[h++]="height: 100%;";f[h++]="margin-right: "+this.b+"px;";f[h++]="}";f[h++]="."+this.Ta()+" {";f[h++]="border: "+
this.Da+"px solid #ccc;";f[h++]="height: 100%;";f[h++]="left: 0;";f[h++]="background-image: url("+fci.friendconnect_imageUrl+"/loading.gif);";f[h++]="background-position: center;";f[h++]="background-repeat: no-repeat;";f[h++]="}";f[h++]="."+this.d("cr")+" {";f[h++]=i(a,"repeat-y");f[h++]="height: 100%;";f[h++]="position:absolute;";f[h++]="right: 0;";f[h++]="top: 0;";f[h++]="width:"+this.b+"px;";f[h++]="}";f[h++]="."+this.d("bl")+" {";f[h++]=i(d,"no-repeat");f[h++]="height: "+this.b+"px;";f[h++]="position:absolute;";
f[h++]="width:"+this.b+"px;";f[h++]="}";f[h++]="."+this.d("tl")+" {";f[h++]=i(e,"no-repeat");f[h++]="height: "+this.b+"px;";f[h++]="position:absolute;";f[h++]="left:0px;";f[h++]="width:"+this.b+"px;";f[h++]="}";f[h++]="."+this.d("bc")+" {";f[h++]=i(c,"repeat-x");f[h++]="height: "+this.b+"px;";f[h++]="left: "+this.b+"px;";f[h++]="position:absolute;";f[h++]="right: "+this.b+"px;";f[h++]="}";f[h++]="."+this.d("tc")+" {";f[h++]=i(b,"repeat-x");f[h++]="height: "+this.b+"px;";f[h++]="left: "+this.b+"px;";
f[h++]="margin-left: "+this.b+"px;";f[h++]="margin-right: "+this.b+"px;";f[h++]="right: "+this.b+"px;";f[h++]="}";f[h++]="."+this.d("br")+" {";f[h++]=i(j,"no-repeat");f[h++]="height: "+this.b+"px;";f[h++]="position:absolute;";f[h++]="right: 0;";f[h++]="width: "+this.b+"px;";f[h++]="}";f[h++]="."+this.d("tr")+" {";f[h++]=i(g,"no-repeat");f[h++]="height: "+this.b+"px;";f[h++]="position:absolute;";f[h++]="right: 0;";f[h++]="top: 0;";f[h++]="width: "+this.b+"px;";f[h++]="}";f[h++]="</style>";return f[fcQ]("")};
fc2[fc].O=function(){var a=['<div id="'+this.t+'"></div>','<div id="'+this.gb+'"></div>','<div id="'+this.ja(this.h.P())+'"></div>'];return a[fcQ]("")};fc2[fc].ub=function(a,b,c,d){this.h.M(a)||(b=new fc3(this,a,b,c,d),c=this.h.P(),d=fcIb(this.ja(c)),fcn(d,b.O()+'<div id="'+this.ja(c+1)+'"></div>'),this.h.set(a,b))};fc2[fc].na=function(a){(a=this.h.get(a))&&a.drawer&&fc1b(a.drawer,!1)};fc2[fc].ic=function(a){if(a=this.h.get(a))a.rendered=!1};
fc2[fc].refresh=function(){for(var a=this.h.z(),b=0;b<a[fcr];b++){var c=a[b];this.na(c);this.ic(c)}};fc2[fc].cc=function(a){for(var b=this.h.F(),c=0;c<b[fcr];c++){var d=b[c];if(d.id==a){d.Fc();break}}};fc2[fc].bc=function(a){for(var b=this.h.F(),c=0;c<b[fcr];c++){var d=b[c];if(d.id==a){d.Zb();break}}};fc2[fc].sc=function(a){if((a=this.h.get(a))&&a.drawer&&a.pa())a.ea(),a.La(),a.Ca()};
fc2[fc].Ec=function(a,b){var c=this.h.get(a);if(c){if(!c.drawer)c.drawer=fcIb(this.ga(c[fcN])),c.target=fcIb(this.R(c[fcN])),c.sha_bc=fcKb(fcj,"div",this.n()=="top"?this.d("bc"):this.d("tc"),c.drawer)[0],c.sha_cr=fcKb(fcj,"div",this.d("cr"),c.drawer)[0];for(var d=this.h.z(),e=0;e<d[fcr];e++){var g=d[e];a!==g&&this.na(g)}c.ea(b);fc1b(c.drawer,!0);fci.setTimeout(function(){c.Ca();c.La();c.render()},0)}};
var fc3=function(a,b,c,d,e){this.id=-1;this.bar=a;this.name=b;this.constraints=d;this.skin=e||{};fco(this,this.skin.HEIGHT||"0");this.url=fci.friendconnect_serverBase+c;this.sha_bc=this.target=this.drawer=fcc;this.loaded=this.rendered=!1;this.ea()};
fc3[fc].ea=function(a){fcDb(this.constraints,a||{});fcDb(this.skin,this.constraints);if(this.bar.fixedIeSizes&&this.constraints.left&&this.constraints[fcya]){var a=this.bar.Bb(),b=this.constraints.left,c=this.constraints[fcya];a-=b+c;a%2&&(a-=1,this.skin.right+=1);fcm(this.skin,a);delete this.skin.left}};
fc3[fc].Ca=function(){if(this.drawer){if(this.skin[fcs]){var a=this.bar.Da,b=this.bar.b,c=fcX?2:0;fcZb(this.target,this.skin[fcs],"");fcZb(this.sha_bc,this.skin[fcs]-b+2*a-c,"");this.skin.rightShadow?fcZb(this.drawer,this.skin[fcs]+b+2*a-c,""):fcZb(this.drawer,this.skin[fcs]+2*a-c,"")}if(this.skin[fcya])this.drawer[fcD].right=this.skin[fcya]+0+"px"}};
fc3[fc].La=function(){if(fcX&&this.drawer){var a=fc0b(this.target),b=a[fcs]-this.bar.b,a=a[fcO];b<0&&(b=0);this.sha_bc&&this.sha_bc[fcD]&&fcZb(this.sha_bc,b,"");this.sha_cr&&this.sha_cr[fcD]&&fcZb(this.sha_cr,"",a)}};
fc3[fc].O=function(){var a="display:none;",b="position: relative; ",c="",d="",e="",g="",j=!!this.skin.rightShadow;j||(c+="display: none; ",e+="display: none; ",d+="right: 0px; ",g+="margin-right: 0px; ");for(var i in this.skin){var k=Number(this.skin[i]);j&&fcKa(i,"width")==0&&(k+=this.bar.b);fcKa(i,"height")==0&&(b+=i+": "+k+"px; ");i!="rightShadow"&&(fcKa(i,"height")==0&&(k+=this.bar.b),fcKa(i,"width")==0&&(k+=2),a+=i+": "+k+"px; ");fcX&&fcKa(i,"width")==0&&(k-=j?2*this.bar.b:this.bar.b,d+=i+": "+
k+"px; ")}fcX&&(this[fcO]|0)>0&&(j=(this[fcO]|0)+2,c+="height: "+j+"px; ");j=0;i=[];i[j++]='<div id="'+this.bar.ga(this[fcN])+'"class="'+this.bar.Pa()+'"style="'+a+'"> ';this.bar.n()=="bottom"&&(i[j++]='<div class="'+this.bar.d("tl")+'"></div> <div class="'+this.bar.d("tc")+'"style="'+d+'"></div> <div class="'+this.bar.d("tr")+'"style="'+e+'"></div> ');i[j++]='<div style="'+b+'"> <div class="'+this.bar.Ua()+'"style="'+g+'"><div id="'+this.bar.R(this[fcN])+'"class="'+this.bar.Ta()+'"></div> <div class="'+
this.bar.d("cr")+'"style="'+c+'"></div> </div> </div> ';this.bar.n()=="top"&&(i[j++]='<div class="'+this.bar.d("bl")+'"></div> <div class="'+this.bar.d("bc")+'"style="'+d+'"></div> <div class="'+this.bar.d("br")+'"style="'+e+'"></div> ');i[j++]="</div> ";return i[fcQ]("")};fc3[fc].Fc=function(){this.rendered=this.pa()};fc3[fc].Zb=function(){this.loaded=this.pa()};fc3[fc].pa=function(){return!!this.drawer&&this.drawer[fcD][fcva]!="none"};
fc3[fc].render=function(){if(this.rendered==!1){var a={};a.url=this.url;a.id=this.bar.R(this[fcN]);a.site=this.bar.site;a["view-params"]=fcT(this.bar.Ic);this[fcN]=="profile"&&(a["view-params"].profileId="VIEWER");this.skin&&fcDb(a["view-params"].skin,this.skin);a["view-params"].menuName=this[fcN];a["view-params"].opaque="true";a["view-params"].menuPosition=this.bar.gc;fco(a,"1px");if(fckc&&fclc&&fc4)this.id=fc4.render(a)}};fcV("google.friendconnect.FriendBar",fc2);var fcoc="0123456789abcdefghijklmnopqrstuv",fcqc=function(a){a=new fcpc(a);a.ra()%5&&fca(fce());for(var b=[],c=0;a.ra()>0;c++)b[c]=fcoc[fcz](a.ec(5));return b[fcQ]("")},fcpc=function(a){this.G=this.r=0;this.da=a};fcpc[fc].ra=function(){return 8*(this.da[fcr]-this.G)-this.r};
fcpc[fc].ec=function(a){var b=0;a>this.ra()&&fca(fce());if(this.r>0){var b=255>>this.r&this.da[this.G],c=8-this.r,d=fcl.min(a%8,c);c-=d;b>>=c;a-=d;this.r+=d;if(this.r==8)this.r=0,this.G++}for(;a>=8;)b<<=8,b|=this.da[this.G],this.G++,a-=8;if(a>0)b<<=a,b|=this.da[this.G]>>8-a,this.r=a;return b};var fcrc=(new Date).getTime(),fc5=function(){},fcsc=function(){},fctc=function(){},fcuc=function(){};fcW(fcuc,fctc);var fcvc=function(a){if(a)for(var b in a)a.hasOwnProperty(b)&&(this[b]=a[b]);if(this.viewParams)for(var c in this.viewParams)/^FC_RELOAD_.*$/[fcia](c)&&(this.viewParams[c]=fcc)};fcvc[fc].render=function(a){var b=this;a&&(b.Gc(),this.Db(function(c){fcVb(a,"visibility","hidden");fcn(a,c);b.refresh(a,c);c=function(a){fcVb(a,"visibility","visible")};c=fcGa(c,a);fcba(c,500);b.chrome=a}))};
fcvc[fc].Db=function(a){return this.Jb(a)};var fc6=function(a){fcvc[fcE](this,a);this.V="../../";this.rpcToken=fch(fcl[fct](2147483647*fcl[fcma]()))};fcW(fc6,fcvc);fc6[fc].lb="gfc_iframe_";fc6[fc].mb="friendconnect";fc6[fc].Ma="";fc6[fc].tc="rpc_relay.html";fc6[fc].Y=function(a){this.V=a};fc6[fc].Gc=function(){return this.Ma=fch(fcl[fct](2147483647*fcl[fcma]()))};fc6[fc].ha=function(){return this.lb+this.Ma+"_"+this.id};
fc6[fc].refresh=function(a,b){var c=fc4.Lc,d,e={},g=fc4.Na(this.communityId),j=g[fcx]("~"),i=fc4.vb;if(i&&j[fcr]>1){d=j[2];var j=j[1],k=[this.specUrl,this.communityId,j,i][fcQ](":");e.sig=fc4.hash(d,k);e.userId=j;e.dateStamp=i}e.container=this.mb;e.mid=this.id;e.nocache=fc4.fc;e.view=this.$;e.parent=fc4.T;this.debug&&(e.debug="1");this.specUrl&&(e.url=this.specUrl);if(this.communityId)i=fcd[fcL][fcB]().profileId,e.communityId=this.communityId,(d=fcd[fcL][fcB]().psinvite)&&(e.psinvite=d),i&&(e.profileId=
i);e.caller=fcwc();e.rpctoken=this.rpcToken;i=!1;d="";j=/Version\/3\..*Safari/;if((j=fc4a&&fc_a()[fcF](j))||!fc4.S[this.specUrl]&&this.viewParams)e["view-params"]=fcd[fcI][fcha](this.viewParams),d="?viewParams="+fcg(e["view-params"]),i=!0;this.prefs&&(e.prefs=fcd[fcI][fcha](this.prefs));this.viewParams&&this.sendViewParamsToServer&&(e["view-params"]=fcd[fcI][fcha](this.viewParams));if(this.locale)e.locale=this.locale;if(this.secureToken)e.st=this.secureToken;j=fc4.Sa(this.specUrl);d=j+"ifr"+d+(this.hashData?
"&"+this.hashData:"");fc4.Kc!=1||i||g||this.secureToken?g&&!e.sig&&(e.fcauth=g):e.sig||(c="get");g=this.ha();fcxc(g,d,c,e,a,b,this.rpcToken)};var fc7=function(){this.j={};this.T="http://"+fcj[fcA].host;this.$="default";this.fc=1;this.Pc=0;this.Mc="US";this.Nc="en";this.Oc=2147483647};fcW(fc7,fcsc);fc7[fc].v=fcvc;fc7[fc].B=new fcuc;fc7[fc].fb=function(a){this.fc=a};fc7[fc].Ka=function(a){this.Kc=a};fc7[fc].Ra=function(a){return"gadget_"+a};fc7[fc].w=function(a){return this.j[this.Ra(a)]};
fc7[fc].N=function(a){return new this.v(a)};fc7[fc].ob=function(a){a.id=this.Kb();this.j[this.Ra(a.id)]=a};fc7[fc].dc=0;fc7[fc].Kb=function(){return this.dc++};var fczc=function(){fc7[fcE](this);this.B=new fcyc};fcW(fczc,fc7);fczc[fc].v=fc6;fczc[fc].X=function(a){a[fcF](/^http[s]?:\/\//)||(a=fcj[fcA][fcna][fcF](/^[^?#]+\//)[0]+a);this.T=a};fczc[fc].I=function(a){var b=this.B.Qa(a);a.render(b)};var fcyc=function(){this.zb={}};fcW(fcyc,fctc);
fcyc[fc].pb=function(a,b){this.zb[a]=b;var c=fcj[fcy](b).className;!c&&c[fcr]==0&&fcea(fcj[fcy](b),"gadgets-gadget-container")};fcyc[fc].Qa=function(a){return(a=this.zb[a.id])?fcj[fcy](a):fcc};var fc8=function(a){fc6[fcE](this,a);a=a||{};this.$=a.view||"profile"};fcW(fc8,fc6);fc8[fc].rb="canvas.html";fc8[fc].xb="/friendconnect/embed/";
var fcwc=function(){var a=fcd[fcL][fcB]().canvas=="1"||fcd[fcL][fcB]().embed=="1",b=fcc;a&&(b=fcd[fcL][fcB]().caller);b||(a=fcj[fcA],b=a.search[fcv](/([&?]?)psinvite=[^&]*(&?)/,function(a,b,e){return e?b:""}),b=a.protocol+"//"+a.hostname+(a.port?":"+a.port:"")+a.pathname+b);return b};fc8[fc].Cc=function(a){this.$=a};fc8[fc].ma=function(){return this.$};fc8[fc].getBodyId=function(){return this.ha()+"_body"};
fc8[fc].Jb=function(a){var b=this.specUrl;b===fcb&&(b="");var b=(fc4.Sa(b)||this.V)+this.tc,c=this.ha();fcd.rpc.setRelayUrl(c,b);b='<div id="'+this.getBodyId()+'"><iframe id="'+c+'" name="'+c;b+=this[fcO]==0?'" style="width:1px; height:1px;':'" style="width:100%;';this.viewParams.opaque&&(b+="background-color:white;");b+='"';b+=' frameborder="0" scrolling="no"';this.viewParams.opaque||(b+=' allowtransparency="true"');b+=this[fcO]?' height="'+this[fcO]+'"':"";b+=this[fcs]?' width="'+this[fcs]+'"':
"";b+="></iframe>";this.showEmbedThis&&(b+='<a href="javascript:void(0);" onclick="google.friendconnect.container.showEmbedDialog(\''+this.divId+"'); return false;\">Embed this</a>");b+="</div>";a(b)};
fc8[fc].Cb=function(){var a=fcwc(),a="canvas=1&caller="+fcg(a),b=fcd[fcL][fcB]().psinvite;b&&(a+="&psinvite="+fcg(b));a+="&site="+fcg(this.communityId);b=fcT(this.viewParams);if(b.skin!=fcc)for(var c=["BG_IMAGE","BG_COLOR","FONT_COLOR","BG_POSITION","BG_REPEAT","ANCHOR_COLOR","FONT_FACE","BORDER_COLOR","CONTENT_BG_COLOR","CONTENT_HEADLINE_COLOR","CONTENT_LINK_COLOR","CONTENT_SECONDARY_TEXT_COLOR","CONTENT_SECONDARY_LINK_COLOR","CONTENT_TEXT_COLOR","ENDCAP_BG_COLOR","ENDCAP_LINK_COLOR","ENDCAP_TEXT_COLOR",
"CONTENT_VISITED_LINK_COLOR","ALTERNATE_BG_COLOR"],d=0;d<c[fcr];d++)delete b.skin[c[d]];b=fcg(fcd[fcI][fcha](b));b=b[fcv]("\\","%5C");return fc4.T+this.rb+"?url="+fcg(this.specUrl)+(a?"&"+a:"")+"&view-params="+b};fc8[fc].D=function(a){a=a||fcf+this.xb+this.communityId;return this.Eb(a,"embed=1")};fc8[fc].C=function(a){return'<iframe src="'+this.D(a)+'" style="height:500px" scrolling="no" allowtransparency="true" border="0" frameborder="0" ></iframe>'};
fc8[fc].Eb=function(a,b){var c=fcg(fcd[fcI][fcha](this.viewParams)),c=c[fcv]("\\","%5C");return a+"?url="+fcg(this.specUrl)+(b?"&"+b:"")+"&view-params="+c};fc8[fc].Nb=function(){var a=fcd[fcL][fcB]().canvas=="1"||fcd[fcL][fcB]().embed=="1",b=fcc;if(a)(b=fcd[fcL][fcB]().caller)||(b="javascript:history.go(-1)");return b};fc8[fc].Ob=function(a){var b=fcc;a=="canvas"?b=this.Cb():a=="profile"&&(b=this.Nb());return b};
var fc9=function(){fczc[fcE](this);fcd.rpc[fcP]("signin",fc5[fc].signin);fcd.rpc[fcP]("signout",fc5[fc].signout);fcd.rpc[fcP]("resize_iframe",fc5[fc].eb);fcd.rpc[fcP]("set_title",fc5[fc].setTitle);fcd.rpc[fcP]("requestNavigateTo",fc5[fc].cb);fcd.rpc[fcP]("api_loaded",fc5[fc].Ba);fcd.rpc[fcP]("createFriendBarMenu",fc5[fc].Ga);fcd.rpc[fcP]("showFriendBarMenu",fc5[fc].hb);fcd.rpc[fcP]("hideFriendBarMenu",fc5[fc].Va);fcd.rpc[fcP]("putReloadViewParam",fc5[fc].Za);fcd.rpc[fcP]("getViewParams",fc5[fc].Ja);
fcd.rpc[fcP]("getContainerBaseTime",fc5[fc].Ia);fcd.rpc[fcP]("openLightboxIframe",fc5[fc].Ya);fcd.rpc[fcP]("showMemberProfile",fc5[fc].jb);fcd.rpc[fcP]("closeLightboxIframe",fcU(this.u,this));fcd.rpc[fcP]("setLightboxIframeTitle",fcU(this.yc,this));fcd.rpc[fcP]("refreshAndCloseIframeLightbox",fcU(this.hc,this));var a=fcAc;a[fcP]();a.kb(this,"load",this.Qb);a.kb(this,"start",this.Rb);this.V="../../";this.X("");this.fb(0);this.Ka(1);this.qa=fcc;this.apiVersion="0.8";this.openSocialSecurityToken=fcc;
this.W="";this.Ha={};this.Yb=fcc;this.Xb=!1;this.vb=this.ac=this.lastIframeLightboxOpenArguments=this.lastLightboxCallback=this.lastLightboxDialog=fcc;this.Lc="post"};fcW(fc9,fczc);fc9[fc].wc=function(a){this.vb=a};fc9[fc].v=fc8;fc9[fc].S={};fc9[fc].Ac=function(a){this.qa=a};fc9[fc].Sa=function(a){var b=fc9[fc].S[a];if(!b)this.qa[fcC]("http://")!==0?(a=this.tb(a),b=["http://",a,this.qa][fcQ]("")):b=this.qa;return b};
fc9[fc].tb=function(a){var b=new SHA1,a=fcpb(a);b.update(a);b=b.digest();return b=fcqc(b)};
var fcBc=function(a,b){var c=b?b:fci.top,d=c.frames;try{if(c.frameElement.id==a)return c}catch(e){}for(c=0;c<d[fcr];++c){var g=fcBc(a,d[c]);if(g)return g}return fcc},fcxc=function(a,b,c,d,e,g,j){var i="gfc_load_"+a,b='<html><head><style type="text/css">body {background:transparent;}</style>'+(fcX?'<script type="text/javascript">window.goback=function(){history.go(-1);};setTimeout("goback();", 0);<\/script>':"")+"</head><body><form onsubmit='window.goback=function(){};return false;' style='margin:0;padding:0;' id='"+
i+"' method='"+c+"' ' action='"+fcd[fcL].escapeString(b)+"'>",k;for(k in d)b+="<input type='hidden' name='"+k+"' value='' >";b+="</form></body></html>";var c=fcBc(a),l;try{l=c[fcw]||c.contentWindow[fcw]}catch(h){e&&g&&(fcn(e,""),fcn(e,g),c=fcBc(a),l=c[fcw]||c.contentWindow[fcw])}j&&fcd.rpc.setAuthToken(a,j);l.open();l.write(b);l.close();a=l[fcy](i);for(k in d)a[k].value=d[k];if(fcX)a.onsubmit();a.submit()};
fc9[fc].yb=function(){var a=fcd[fcL][fcB]().fcsite,b=fcd[fcL][fcB]().fcprofile;a&&b&&fc4.za(b,a)};fc9[fc].xc=function(a,b){this.S[a]=b};fc9[fc].U=function(){var a=/Version\/3\..*Safari/;if(a=fc4a&&fc_a()[fcF](a))fcj[fcA].reload();else{fc4.g!=fcc&&fc4.g.refresh();for(var b in fc4.j)a=fc4.j[b],this.I(a);if(this.lastIframeLightboxOpenArguments!=fcc)b=this.lastIframeLightboxOpenArguments,this.u(),this.sa[fcM](this,b)}};
fc9[fc].X=function(a){a[fcF](/^http[s]?:\/\//)||(a=a&&a[fcr]>0&&a[fcoa](0,1)=="/"?fcj[fcA][fcna][fcF](/^http[s]?:\/\/[^\/]+\//)[0]+a[fcoa](1):fcj[fcA][fcna][fcF](/^[^?#]+\//)[0]+a);this.T=a};fc9[fc].fa=function(a){return"fcauth"+a};fc9[fc].la=function(a){return"fcauth"+a+"-s"};fc9[fc].hash=function(a,b){var c=new SHA1,d=fcwb(a,!0),c=new G_HMAC(c,d,64),d=fcpb(b),c=c.Ib(d);(new Date).getTime();return fcvb(c,!0)};
fc9[fc].Na=function(a){return a=fcyb.get(this.fa(a))||fcyb.get(this.la(a))||this.Ha[a]||""};fc9[fc].Y=function(a){this.V=a};fc9[fc].Bc=function(a){this.W=a};fc9[fc].N=function(a){a=new this.v(a);a.Y(this.V);return a};fc9[fc].ma=function(){return this.$};fc9[fc].zc=function(a){this.ac=a};var fc$=function(a){return(a=a[fcF](/_([0-9]+)$/))?fcaa(a[1],10):fcc};
fc9[fc].Z=function(a,b,c,d,e,g){if(!this.Jc)this.aa(fci.friendconnect_serverBase+"/friendconnect/styles/container.css?d="+this.W),this.Jc=!0;var j=fcCc(d);if(this.Yb!=(j?"rtl":"ltr"))this.aa(fci.friendconnect_serverBase+"/friendconnect/styles/lightbox"+(j?"-rtl":"")+".css?d="+this.W),this.Yb=j?"rtl":"ltr";if(!this.Xb)this.qb(fci.friendconnect_serverBase+"/friendconnect/script/lightbox.js?d="+this.W),this.Xb=!0;b=b||0;if(goog.ui&&goog.ui[fcka]){this.u();var b=new goog.ui[fcka]("lightbox-dialog",!0),
i=this;goog.events.listen(b,goog.ui[fcka].EventType.AFTER_HIDE,function(){i.lastLightboxCallback&&i.lastLightboxCallback();i.Fa()});b.setDraggable(!0);b.setDisposeOnHide(!0);b.setBackgroundElementOpacity(0.5);b.setButtonSet(new goog.ui[fcka].ButtonSet);this.lastLightboxDialog=b;this.lastLightboxCallback=c||fcc;c=b.getDialogElement();e=e||702;fcVb(c,"width",fch(e)+"px");g&&fcVb(c,"height",fch(g)+"px");a(b);b.getDialogElement()[fcD].direction=j?"rtl":"ltr"}else b<5?(b++,a=fcU(this.Z,this,a,b,c,d,e,
g),fcba(a,1E3)):(this.Fa(),fca(fce("lightbox.js failed to load")))};fc9[fc].u=function(a){var b=this.lastLightboxDialog,c=this.lastLightboxCallback;this.lastLightboxCallback=fcc;b!=fcc&&(this.lastLightboxDialog.dispatchEvent(goog.ui[fcka].EventType.AFTER_HIDE),b.dispose(),c!=fcc&&c(a))};fc9[fc].Fa=function(){this.lastIframeLightboxOpenArguments=this.lastLightboxCallback=this.lastLightboxDialog=fcc};fc9[fc].yc=function(a){this.lastLightboxDialog&&this.lastLightboxDialog.setTitle(a)};
fc9[fc].hc=function(){this.u();this.U()};fc5[fc].cb=function(a,b){var c=fc$(this.f),c=fc4.w(c),d=fcT(c.originalParams);b&&(d["view-params"]=d["view-params"]||{},d["view-params"]=b);d.locale=c.locale;if(c.useLightBoxForCanvas)d.presentation=a,fc4.lastLightboxDialog!=fcc?fc4.u():fc4.ib(d);else if((c=c.Ob(a))&&fcj[fcA][fcna]!=c)if(fcd[fcL][fcB]().embed=="1")try{fci.parent.location=c}catch(e){fci.top.location=c}else fcj[fcA].href=c};
fc9[fc].ib=function(a,b){var a=a||{},c=a.locale,d=fcCc(c),e=this;this.u();this.Z(function(b){var c=fc0("div",{},fc0("div",{id:"gadget-signin",style:"background-color:#ffffff;height:32px;"}),fc0("div",{id:"gadget-lb-canvas",style:"background-color:#ffffff;"}));b.getTitleTextElement()[fcp](fc0("div",{id:"gfc-canvas-title",style:"color:#000000;"}));b.getContentElement()[fcp](c);b.setVisible(!0);var c=fcT(a),i=fcNb(fci),k=fcl[fct](i[fcO]*0.7),i={BORDER_COLOR:"#cccccc",ENDCAP_BG_COLOR:"#e0ecff",ENDCAP_TEXT_COLOR:"#333333",
ENDCAP_LINK_COLOR:"#0000cc",ALTERNATE_BG_COLOR:"#ffffff",CONTENT_BG_COLOR:"#ffffff",CONTENT_LINK_COLOR:"#0000cc",CONTENT_TEXT_COLOR:"#333333",CONTENT_SECONDARY_LINK_COLOR:"#7777cc",CONTENT_SECONDARY_TEXT_COLOR:"#666666",CONTENT_HEADLINE_COLOR:"#333333"};c.id="gadget-lb-canvas";fco(c,fcl.min(498,k)+"px");c.maxHeight=k;c.keepMax&&(fco(c,k),fcVb(b.getContentElement(),"height",k+35+"px"));c["view-params"]=c["view-params"]||{};c["view-params"].opaque=!0;c["view-params"].skin=c["view-params"].skin||{};
fcHa(c["view-params"].skin,i);e.render(c);k={id:"gadget-signin",presentation:"canvas"};k.site=c.site;k.titleDivId="gfc-canvas-title";k["view-params"]={};k["view-params"].opaque=!0;k.keepMax=c.keepMax;c.securityToken&&(k.securityToken=c.securityToken);c=fcT(i);c.ALIGNMENT=d?"left":"right";e.ab(k,c);b.reposition()},fcb,b,c)};fc5[fc].hb=function(a,b){fc4.g!=fcc&&fc4.g.Ec(a,b)};fc5[fc].Va=function(a){fc4.g!=fcc&&fc4.g.na(a)};
fc5[fc].Ya=function(a,b,c,d,e,g,j,i,k,l){var h=this.f,a=a+(a[fcC]("?")>=0?"&":"?")+"iframeId="+h;fc4.sa(a,b,c,d,e,g,j,i,k,l,this.callback)};
fc9[fc].sa=function(a,b,c,d,e,g,j,i,k,l,h){var f=fcNb(fci);d!=fcc||(d=fcl[fct](f[fcO]*0.7));c!=fcc||(c=fcl[fct](f[fcs]*0.7));for(var m=[],f=0;f<arguments[fcr]&&f<10;f++)m[fcq](arguments[f]);!a[0]=="/"&&fca(fce("lightbox iframes must be relative to fc server"));var p=this,n=g?fcT(g):{},q=fch(fcl[fct](2147483647*fcl[fcma]())),o="gfc_lbox_iframe_"+q;fcd.rpc.setAuthToken(o,q);if(!b)b=fc4.openSocialSecurityToken;var r=fc4.openSocialSiteId;fc4.Z(function(c){p.lastIframeLightboxOpenArguments=m;var g="st="+
fcg(b)+"&parent="+fcg(fc4.T)+"&rpctoken="+fcg(q);if(!i)n.iframeId=o,n.iurl=a,a=fcf+"/friendconnect/lightbox";var f=d-54;fco(n,f);var h='<iframe id="'+o;h+='" width="100%" height="'+f+'" frameborder="0" scrolling="auto"></iframe>';c.setContent(h);e&&(c.setTitle(e),l&&(f=c.getTitleTextElement(),fcHb(f,"lightbox-dialog-title-small-text")));c.setVisible(!0);k||(n.fcauth=fc4.Na(r));a+=(a[fcC]("?")>=0?"&":"?")+g+"&communityId="+r;fcxc(o,a,"POST",n,fcc,fcc,fcc)},fcb,h,fcb,c,d)};
fc5[fc].Ja=function(){var a=fc$(this.f),a=fc4.w(a);return a.viewParams};fc5[fc].Ia=function(){return fcrc};fc5[fc].Za=function(a,b){var c=fc$(this.f),c=fc4.w(c);c.viewParams[a]=b};fc9[fc].Qb=function(a,b){fc4.g!=fcc&&fc4.g.bc(b)};fc9[fc].Rb=function(a,b){fc4.g!=fcc&&fc4.g.cc(b)};fc5[fc].Ga=function(a,b,c,d){fc4.g!=fcc&&fc4.g.ub(a,b,c,d)};fc9[fc].I=function(a){var b=this.B.Qa(a);a.render(b);this.B.postProcessGadget&&this.B.postProcessGadget(a)};
fc5[fc].signout=function(a){fc4.$a(fc4.fa(a));fc4.$a(fc4.la(a));fc4.Ha={};fc4.U();return!1};fc9[fc].$a=function(a){for(var b=fcj[fcA].pathname,b=b[fcx]("/"),c=0;c<b[fcr];c++){for(var d=fck(c+1),e=0;e<c+1;e++)d[e]=b[e];fcyb.remove(a,d[fcQ]("/")+"/")}};
fc5[fc].eb=function(a){var b=fcj[fcy](this.f);b&&a>0&&fco(b[fcD],a+"px");(b=fcj[fcy](this.f+"_body"))&&a>0&&fco(b[fcD],a+"px");if(b=fc$(this.f)){var c=fc4.w(b);if(c){if((b=fcj[fcy](c.divId))&&a>0){if(c&&c[fcpa]&&c[fcpa]<a)a=c[fcpa],b[fcD].overflowY="auto";fco(b[fcD],a+"px")}!c.keepMax&&c.ma()=="canvas"&&fc4.lastLightboxDialog&&fc4.lastLightboxDialog.reposition();fcVb(c.chrome,"visibility","visible")}}};fc5[fc].setTitle=function(a){var b=fc$(this.f),b=fc4.w(b);(b=b.titleDivId)&&fcn(fcj[fcy](b),fcd[fcL].escapeString(a))};
fc5[fc].signin=function(a,b,c){fcyb.set(fc4.fa(a),b,31104E3,c);fcyb.set(fc4.la(a),b,-1,c);fc4.Ha[a]=b;fc4.U()};var fcEc=function(a){fcgc(a,fcDc)};fc9[fc].nc=function(a,b){b&&this.m(b,a);var c={};c.url=fcf+"/friendconnect/gadgets/members.xml";this.render(this.s(a,c))};fc9[fc].pc=function(a,b){b&&this.m(b,a);var c={};c.url=fcf+"/friendconnect/gadgets/review.xml";c["view-params"]={startMaximized:"true",disableMinMax:"true",features:"review"};this.render(this.s(a,c))};
fc9[fc].ua=function(a,b){b&&this.m(b,a);var c={};c.url=fcf+"/friendconnect/gadgets/wall.xml";c["view-params"]={startMaximized:"true",disableMinMax:"true",features:"comment"};this.render(this.s(a,c))};fc9[fc].ab=function(a,b){b&&this.m(b,a);var c={};c.url=fcf+"/friendconnect/gadgets/signin.xml";fco(c,32);this.render(this.s(a,c))};
fc9[fc].kc=function(a,b){b&&this.m(b,a);a.prefs=a.prefs||{};a.sendViewParamsToServer=!0;a.prefs.hints=fci.google_hints;var c={};c.url=fcf+"/friendconnect/gadgets/ads.xml";fco(c,90);this.render(this.s(a,c))};fc9[fc].xa=function(a,b){if(a.id){b&&this.m(b,a);a["view-params"]=a["view-params"]||{};a["view-params"].opaque="true";this.g=new fc2(a);this.g.render();var c={};c.url=fcf+"/friendconnect/gadgets/friendbar.xml";a.id=this.g.t;fco(a,"1");this.render(this.s(a,c))}};fc9[fc].mc=fc9[fc].xa;
fc9[fc].wa=function(a,b){a=a||{};a.url=fcf+"/friendconnect/gadgets/signin.xml";a.site=a.site||fcd[fcL][fcB]().site;fco(a,32);this.va(a,b)};fc9[fc].lc=fc9[fc].wa;fc9[fc].rc=fc9[fc].ua;fc9[fc].m=function(a,b){var c=b["view-params"];c||(c={},b["view-params"]=c);c.skin=a};fc9[fc].s=function(a,b){var c=this.Xa(b,a);if(b["view-params"]){var d=b["view-params"];a["view-params"]&&(d=this.Xa(d,a["view-params"]));c["view-params"]=d}return c};fc9[fc].oc=function(a,b){b&&this.m(b,a);this.render(a)};
fc9[fc].Xa=function(a,b){var c={},d;for(d in b)c[d]=b[d];for(d in a)typeof c[d]=="undefined"&&(c[d]=a[d]);return c};
fc9[fc].render=function(a){this.openSocialSiteId=a.site;a["view-params"]=a["view-params"]||{};var b=this.N({divId:a.id,specUrl:a.url,communityId:a.site,height:a[fcO],locale:a.locale||this.ac,secureToken:a.securityToken,titleDivId:a.titleDivId,showEmbedThis:a.showEmbedThis,useLightBoxForCanvas:a.useLightBoxForCanvas||typeof a.useLightBoxForCanvas=="undefined"&&fci.friendconnect_lightbox,viewParams:a["view-params"],prefs:a.prefs,originalParams:a,debug:a.debug,maxHeight:a[fcpa],sendViewParamsToServer:a.sendViewParamsToServer,
keepMax:a.keepMax});a.presentation&&b.Cc(a.presentation);this.ob(b);this.B.pb(b.id,a.id);fcba(function(){fc4.I(b)},0);return b.id};fc9[fc].qc=function(a,b){a=a||{};a.presentation="canvas";this.bb(a,b)};
fc9[fc].bb=function(a,b,c){a=a||{};a.url=fcd[fcL][fcB]().url;a.site=fcd[fcL][fcB]().site||a.site;var d=fcd[fcL][fcB]()["view-params"];d&&(a["view-params"]=fcd[fcI].parse(decodeURIComponent(d)));if(c)a["view-params"]=a["view-params"]||{},a["view-params"].useFixedHeight=!0,fco(a["view-params"],c),b=b||{},b.HEIGHT=fch(c);this.va(a,b)};fc9[fc].va=function(a,b){a=a||{};b&&this.m(b,a);fcd[fcL][fcB]().canvas=="1"?a.presentation="canvas":fcd[fcL][fcB]().embed=="1"&&(a.presentation="embed");fc4.render(a)};
fc9[fc].Pb=function(){var a=fcd[fcL][fcB]().caller;a&&fcj[fcA][fcna]!=a&&a[fcr]>8&&(a.substr(0,7)[fcxa]()=="http://"||a.substr(0,8)[fcxa]()=="https://")?fcj[fcA].href=a:(a=fcd[fcL][fcB]().site)?fcj[fcA].href=fcf+"/friendconnect/directory/site?id="+a:fci.history.go(-1)};fc9[fc].H="";fc9[fc].Lb=function(){return this.H};fc9[fc].uc=function(a){this.apiVersion=a};fc9[fc].aa=function(a){var b=fcj[fcH]("link");b[fcK]("rel","stylesheet");b[fcK]("type","text/css");b[fcK]("href",a);fcj.getElementsByTagName("head")[0][fcp](b)};
fc9[fc].qb=function(a){var b=fcj[fcH]("script");b[fcK]("src",a);b[fcK]("type","text/javascript");fcj.getElementsByTagName("head")[0][fcp](b)};fc9[fc].Ea=function(a){fcj[fcla]?a():fci[fcJ]?fci[fcJ]("load",a,!1):fci.attachEvent("onload",a)};fc9[fc].oa=function(a){a.site||fca("API not loaded, please pass in a 'site'");this.aa(fci.friendconnect_serverBase+"/friendconnect/styles/container.css?d="+this.W);this.openSocialSiteId=a.site;this.apiLoadedCallback=a.onload;this.Ea(fcU(this.Wa,this,a,"fc-opensocial-api"))};
fc9[fc].$b=fc9[fc].oa;fc9[fc].Sb=function(a){var b={};b.site=this.openSocialSiteId;b["view-params"]={txnId:a};this.Wa(b,"gfc-"+a)};fc9[fc].jc=function(a){var b={},c;for(c in this.j){var d=this.j[c];if(d.viewParams&&d.viewParams.txnId==a)break;else b[c]=d}this.j=b;(a=fcj[fcy]("gfc-"+a))&&a.parentNode&&a.parentNode.removeChild&&a.parentNode.removeChild(a)};fc9[fc].Fb=function(){return"<Templates xmlns:fc='http://www.google.com/friendconnect/makeThisReal'>  <Namespace prefix='fc' url='http://www.google.com/friendconnect/makeThisReal'/>  <Template tag='fc:signIn'>    <div onAttach='google.friendconnect.renderSignInButton({element: this})'></div>  </Template></Templates>"};
fc9[fc].Mb=function(){return"<Templates xmlns:os='http://ns.opensocial.org/2008/markup'><Namespace prefix='os' url='http://ns.opensocial.org/2008/markup'/><Template tag='os:Name'>  <span if='${!My.person.profileUrl}'>${My.person.displayName}</span>  <a if='${My.person.profileUrl}' href='${My.person.profileUrl}'>      ${My.person.displayName}</a></Template><Template tag='os:Badge'>  <div><img if='${My.person.thumbnailUrl}' src='${My.person.thumbnailUrl}'/>   <os:Name person='${My.person}'/></div></Template><Template tag='os:PeopleSelector'>  <select onchange='google.friendconnect.PeopleSelectorOnChange(this)' name='${My.inputName}'          multiple='${My.multiple}' x-var='${My.var}' x-max='${My.max}'          x-onselect='${My.onselect}'>    <option repeat='${My.group}' value='${Cur.id}' selected='${Cur.id == My.selected}'>        ${Cur.displayName}    </option>  </select></Template></Templates>"};
var fcFc=function(a){var b;if(a.multiple){b=[];for(var c=0;c<a[fcG][fcr];c++)a[fcG][c].selected&&b[fcq](a[fcG][c].value);c=a.getAttribute("x-max");c*=1;if(c&&b[fcr]>c&&a["x-selected"]){b=a["x-selected"];for(c=0;c<a[fcG][fcr];c++){a[fcG][c].selected=!1;for(var d=0;d<b[fcr];d++)if(a[fcG][c].value==b[d]){a[fcG][c].selected=!0;break}}}}else b=a[fcG][a.selectedIndex].value;a["x-selected"]=b;(c=a.getAttribute("x-var"))&&fci.opensocial[fcja]&&fci.opensocial[fcja].getDataContext().putDataSet(c,b);if(c=a.getAttribute("x-onselect"))if(fci[c]&&
typeof fci[c]=="function")fci[c](b);else a["x-onselect-fn"]?a["x-onselect-fn"][fcM](a):a["x-onselect-fn"]=new Function(c)};
fc9[fc].Wa=function(a,b){fci.opensocial.template.Loader.loadContent(this.Mb());fci.opensocial.template.Loader.loadContent(this.Fb());fci.opensocial[fcja].processDocumentMarkup();var c=fcj[fcH]("div");c.id=b;fco(c[fcD],"0px");fcm(c[fcD],"0px");c[fcD].position="absolute";c[fcD].visibility="hidden";fcj[fcla][fcp](c);var d={};d.url=fcf+"/friendconnect/gadgets/osapi-"+this.apiVersion+".xml";fco(d,0);d.id=c.id;d.site=a.site;d["view-params"]=a["view-params"];this.render(d)};
fc5[fc].Ba=function(){fc4.H=this.f;fc4.openSocialSecurityToken=this.a[0];var a=fc4.openSocialSecurityToken;fci.opensocial[fcja].executeRequests();fci.opensocial.template.process();fc4.apiLoadedCallback&&(a=fcGa(fc4.apiLoadedCallback,a),fcba(a,0))};fc9[fc].Q=function(a){var b=fcc,c;for(c in this.j)if(this.j[c].divId==a){b=this.j[c];break}return b};fc9[fc].D=function(a,b){var c=this.Q(a),d=fcc;c&&(d=c.D(b));return d};fc9[fc].C=function(a,b){var c=this.Q(a),d=fcc;c&&(d=c.C(b));return d};
fc9[fc].Dc=function(a,b){this.Z(function(c){var d=fcj.createTextNode("Copy & paste this code into your site.");c.getContentElement()[fcp](d);c.getContentElement()[fcp](fcj[fcH]("br"));var d=fc4.C(a,b),e=fcj[fcH]("textarea");fcn(e,d);e[fcK]("style","width:500px;");c.getContentElement()[fcp](e);c.setVisible(!0)})};var fcGc=["ar","dv","fa","iw","he","ku","pa","sd","tk","ug","ur","yi"],fcCc=function(a){var b=!1;a?(a=a[fcx]("_")[0],b=fclb(fcGc,a)):b=(a=fcWb(fcj[fcla],"direction"))&&a=="rtl";return b};
fc5[fc].jb=function(a,b){var c=0,d=fcc;try{var e=fc$(this.f),g=fc4.w(e),d=g.secureToken,c=g.communityId}catch(j){}b&&(c=b);fc4.za(a,c,this.callback,d)};fc9[fc].za=function(a,b,c,d){b=b||this.openSocialSiteId;a={keepMax:!0,presentation:"canvas",url:fcf+"/friendconnect/gadgets/members.xml",site:b,"view-params":{profileId:a}};d&&(a.securityToken=d);this.ib(a,c)};fc9[fc].Hb=function(a){var b=fcc;if((a=this.Q(a))&&a.secureToken)b=a.secureToken;return b};
fc9[fc].Gb=function(a){var b=fcc;if((a=this.Q(a))&&a.communityId)b=a.communityId;return b};var fcDc=function(a){fc4.H&&fcac(fc4.H,a)},fcHc=function(){fc5[fc].signout(fc4.openSocialSiteId)},fcIc=function(){fcdc(fc4.H)},fcJc=function(a,b){fc8b(a,b)},fcmc=function(){this.o={}};fcmc[fc].register=function(){fcd.rpc[fcP]("subscribeEventType",fc5[fc].subscribe);fcd.rpc[fcP]("publishEvent",fc5[fc].publish)};fc5[fc].subscribe=function(a){var b=fcAc;b.o[a]=b.o[a]||[];a=b.o[a];a[a[fcr]]={frameId:this}};
fcmc[fc].kb=function(a,b,c){var d=this;d.o[b]=d.o[b]||[];b=d.o[b];b[b[fcr]]={container:a,callback:c}};fc5[fc].publish=function(a){var b=fcAc,c=0;this.f&&(c=fc$(this.f));b.o[a]=b.o[a]||[];for(var b=b.o[a],d=0;d<b[fcr];d++)b[d].container?b[d].callback[fcE](b[d].container,a,c):fcd.rpc[fcE](b[d].frameId,a,fcc,a,c)};var fcnc=fcU(fc5[fc].publish,new fc5),fcAc=new fcmc,fc4=new fc9;fc4.Ea(fc4.yb);fcV("google.friendconnect.container",fc4);fcV("google.friendconnect.container.refreshGadgets",fc4.U);
fcV("google.friendconnect.container.setParentUrl",fc4.X);fcV("google.friendconnect.container.setServerBase",fc4.Y);fcV("google.friendconnect.container.setServerVersion",fc4.Bc);fcV("google.friendconnect.container.createGadget",fc4.N);fcV("google.friendconnect.container.openLightboxIframe",fc4.sa);fcV("google.friendconnect.container.renderGadget",fc4.I);fcV("google.friendconnect.container.render",fc4.render);fcV("google.friendconnect.container.goBackToSite",fc4.Pb);
fcV("google.friendconnect.container.renderMembersGadget",fc4.nc);fcV("google.friendconnect.container.renderReviewGadget",fc4.pc);fcV("google.friendconnect.container.renderCommentsGadget",fc4.ua);fcV("google.friendconnect.container.renderSignInGadget",fc4.ab);fcV("google.friendconnect.container.renderFriendBar",fc4.mc);fcV("google.friendconnect.container.renderSocialBar",fc4.xa);fcV("google.friendconnect.container.renderCanvasSignInGadget",fc4.lc);
fcV("google.friendconnect.container.renderUrlCanvasGadget",fc4.qc);fcV("google.friendconnect.container.renderEmbedSignInGadget",fc4.wa);fcV("google.friendconnect.container.renderUrlEmbedGadget",fc4.bb);fcV("google.friendconnect.container.renderEmbedGadget",fc4.va);fcV("google.friendconnect.container.renderWallGadget",fc4.rc);fcV("google.friendconnect.container.renderAdsGadget",fc4.kc);fcV("google.friendconnect.container.renderOpenSocialGadget",fc4.oc);
fcV("google.friendconnect.container.setNoCache",fc4.fb);fcV("google.friendconnect.container.enableProxy",fc4.Ka);fcV("google.friendconnect.container.setDomain",fc4.xc);fcV("google.friendconnect.container.setLockedDomainSuffix",fc4.Ac);fcV("google.friendconnect.container.setLocale",fc4.zc);fcV("google.friendconnect.container.loadOpenSocialApi",fc4.$b);fcV("google.friendconnect.container.initOpenSocialApi",fc4.oa);fcV("google.friendconnect.container.getOpenSocialApiIframeId",fc4.Lb);
fcV("google.friendconnect.container.setApiVersion",fc4.uc);fcV("google.friendconnect.container.getEmbedUrl",fc4.D);fcV("google.friendconnect.container.getEmbedHtml",fc4.C);fcV("google.friendconnect.container.getGadgetSecurityToken",fc4.Hb);fcV("google.friendconnect.container.getGadgetCommunityId",fc4.Gb);fcV("google.friendconnect.container.showEmbedDialog",fc4.Dc);fcV("google.friendconnect.container.showMemberProfile",fc4.za);fcV("google.friendconnect.requestSignIn",fcDc);
fcV("google.friendconnect.requestSignOut",fcHc);fcV("google.friendconnect.requestSettings",fcIc);fcV("google.friendconnect.requestInvite",fcJc);fcV("google.friendconnect.renderSignInButton",fcEc);fcV("google.friendconnect.container.invokeOpenSocialApiViaIframe",fc4.Sb);fcV("google.friendconnect.container.removeOpenSocialApiViaIframe",fc4.jc);fcV("google.friendconnect.userAgent.WEBKIT",fc4a);fcV("google.friendconnect.userAgent.IE",fcX);fcV("google.friendconnect.PeopleSelectorOnChange",fcFc);
fcV("google.friendconnect.container.setDateStamp_",fc4.wc);
google.friendconnect.container.setServerBase('http://www-a-fc-opensocial.googleusercontent.com/ps/');google.friendconnect.container.setServerVersion('0.560.7');google.friendconnect.container.setApiVersion('0.8');
google.friendconnect.container.setDomain('http://www.google.com/friendconnect/gadgets/activities.xml', 'http://q8j0igk2u2f6kf7jogh6s66md2d7r154-a-fc-opensocial.googleusercontent.com/ps/');

google.friendconnect.container.setDomain('http://www.google.com/friendconnect/gadgets/ads.xml', 'http://t767uouk8skpac15v8ue0n16regb3m2t-a-fc-opensocial.googleusercontent.com/ps/');

google.friendconnect.container.setDomain('http://www.google.com/friendconnect/gadgets/ask.xml', 'http://uofgf6lm45rimd9jp6hn983ul6n2en81-a-fc-opensocial.googleusercontent.com/ps/');

google.friendconnect.container.setDomain('http://www.google.com/friendconnect/gadgets/friendbar.xml', 'http://p7rjrrl49ose4gob99eonlvp0drmce3d-a-fc-opensocial.googleusercontent.com/ps/');

google.friendconnect.container.setDomain('http://www.google.com/friendconnect/gadgets/content_reveal.xml', 'http://n0mc7q283f00tpk3uifa7sjv4hmrults-a-fc-opensocial.googleusercontent.com/ps/');

google.friendconnect.container.setDomain('http://www.google.com/friendconnect/gadgets/chat.xml', 'http://4mmefl67as0q39gf1o4pnocubqmdgei0-a-fc-opensocial.googleusercontent.com/ps/');

google.friendconnect.container.setDomain('http://www.google.com/friendconnect/gadgets/donate.xml', 'http://7v4nflqvq38notsghmcr5a9t6o9g6kn4-a-fc-opensocial.googleusercontent.com/ps/');

google.friendconnect.container.setDomain('http://www.google.com/friendconnect/gadgets/lamegame.xml', 'http://ffbrstu9puk7qmt45got9mqe5k7ijrs4-a-fc-opensocial.googleusercontent.com/ps/');

google.friendconnect.container.setDomain('http://www.google.com/friendconnect/gadgets/map.xml', 'http://k0dfp8trn0hi5d2spmo7jmc88n6kr1cc-a-fc-opensocial.googleusercontent.com/ps/');

google.friendconnect.container.setDomain('http://www.google.com/friendconnect/gadgets/members.xml', 'http://r1rk9np7bpcsfoeekl0khkd2juj27q3o-a-fc-opensocial.googleusercontent.com/ps/');

google.friendconnect.container.setDomain('http://www.google.com/friendconnect/gadgets/newsletterSubscribe.xml', 'http://k830suiki828goudg9448o6bp0tpu5r3-a-fc-opensocial.googleusercontent.com/ps/');

google.friendconnect.container.setDomain('http://www.google.com/friendconnect/gadgets/poll.xml', 'http://1ivjd75aqp679vbgohjv74tlhn13rgdu-a-fc-opensocial.googleusercontent.com/ps/');

google.friendconnect.container.setDomain('http://www.google.com/friendconnect/gadgets/recommended_pages.xml', 'http://iqavu79a908u5vcecp0pq80hhbhkv33b-a-fc-opensocial.googleusercontent.com/ps/');

google.friendconnect.container.setDomain('http://www.google.com/friendconnect/gadgets/review.xml', 'http://r85jiaoot111joesr8bilfhfeslcc496-a-fc-opensocial.googleusercontent.com/ps/');

google.friendconnect.container.setDomain('http://www.google.com/friendconnect/gadgets/sample.xml', 'http://785aoao97uti9iqffknjp5e0kn2ljlm4-a-fc-opensocial.googleusercontent.com/ps/');

google.friendconnect.container.setDomain('http://www.google.com/friendconnect/gadgets/signin.xml', 'http://8fkcem1ves287v3g5lu9gep1j91p3kk1-a-fc-opensocial.googleusercontent.com/ps/');

google.friendconnect.container.setDomain('http://www.google.com/friendconnect/gadgets/wall.xml', 'http://o29lt44ell30t7ljcdfr8lq2mjakv2co-a-fc-opensocial.googleusercontent.com/ps/');

google.friendconnect.container.setDomain('http://www.google.com/friendconnect/gadgets/osapi-0.8.xml', 'http://mc8tdi0ripmbpds25eboaupdulritrp6-a-fc-opensocial.googleusercontent.com/ps/');

google.friendconnect.container.setLockedDomainSuffix('-a-fc-opensocial.googleusercontent.com/ps/');
window['__ps_loaded__'] = true; 
 }google.friendconnect_ = google.friendconnect;
google.friendconnect.container.setDateStamp_('133b3739827');