#! /bin/bash

# main
USAGE="\
Usage: generate_bindings path_to_clarabel_source_package path_to_jextract_binary"

# read command line arguments
if [ $# -eq 2 ]; then
  CLARABEL4J=$(dirname "${0}")/../
  CLARABEL="${1}"
  JEXTRACT="${2}"
else
  echo "$USAGE"
  exit 1
fi

# remove old bindings
rm -rf "${CLARABEL4J}"/src/main/java/com/ustermetrics/clarabel4j/bindings/

# generate bindings
$JEXTRACT \
  --target-package com.ustermetrics.clarabel4j.bindings \
  --output "${CLARABEL4J}"/src/main/java \
  @"${CLARABEL4J}"/bindings/includes.txt "${CLARABEL}"/include/Clarabel.h
