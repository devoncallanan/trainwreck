# trainwreck
1186 - Software Engineering


## Gradle use

install:

https://gradle.org/install/  (manual installation)

commands:

  ```gradle build```

  ```gradle run```


## Git/Github use
It is always good to make sure master is up to date by running ```git pull``` while in the master branch.  Then switch to your branch or create a new branch and make sure it is up to date as well before you start making changes.

checkout your branch:
 ```git checkout <branchname>```

create a branch:
* On web interface select branch dropdown then type name in text field and click the create branch option
* On git bash (if you are a cool hacker)
  * ```git checkout -b <branchname>```
  * ```git push -u origin <branchname>```
 
update your branch with master:
1. ```git checkout master```
2. ```git pull```
3. ```git checkout <branchname>```
5. ```git commit -a -m "<message>"```
6. ```git push```
4. ```git merge origin master```

You only need steps 5 and 6 if you have untracked changes, but its a good idea to do it to be safe

commiting:
* add all files ```git add *```
* add specific files ```git add <filename>```
* commit with message ```git commit -m "<message>"```
* push to github ```git push origin```
