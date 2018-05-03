#!/bin/sh
#
# Author : jitwxs <jitwxs@foxmail.com>
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
# the GNU General Public License for more details.
#
# Test rm command with some basic options.

TST_CNT=4
TST_SETUP=setup
TST_TESTFUNC=do_test
TST_NEEDS_TMPDIR=1
TST_NEEDS_CMDS="rm"
. tst_test.sh

setup()
{
	ROD touch "demoFile"
	ROD mkdir "demoDir"
}

rm_test()
{
	local rm_opt=$1
	local rm_file=$2

	local rm_cmd="rm $rm_opt $rm_file"

	eval $rm_cmd > temp 2>&1
	if [ $? -ne 0 ]; then
		grep -q -E "unknown option|invalid option" temp
		if [ $? -eq 0 ]; then
			tst_res TCONF "$rm_cmd not supported."
		else
			tst_res TFAIL "$rm_cmd failed."
		fi
		return
	fi

	if [ -z $rm_opt ];then
		if [ -f $rm_file ];then
			tst_res TFAIL "$rm_cmd failed."
			return
		fi
	else
		if [ $rm_opt = "-r" ];then
			if [ -d $rm_file ];then
				tst_res TFAIL "$rm_cmd failed."
				return
			fi
		fi
	fi

	tst_res TPASS "rm passed with $rm_opt option."
}

do_test()
{
	case $1 in
	1) rm_test "" "demoFile";;
	2) rm_test "-r" "demoDir";;
	3) rm_test "--help";;
	4) rm_test "--version";;
	esac
}

tst_run
