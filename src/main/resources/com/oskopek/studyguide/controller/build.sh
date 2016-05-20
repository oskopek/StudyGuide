#!/bin/bash

asciidoctor *.adoc
for i in `ls *.html`; do
    tmp=`mktemp`
    grep -Ev '<link rel=\"stylesheet\".*' "$i" > "$tmp" # the font stylesheet doesn't play well with JavaFX's WebView
    mv "$tmp" "$i"
done
