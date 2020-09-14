# expression-parser
The goal of this repository is to write a simple expression parser which will basically help the end user validate/parse a simple mathematical expression like (A+B)/(C-D)

## toString()
Parsed expression node should be able to print a tree in a properly nested fashion for example: `(A + B) / (C + CC + CCC)`
```
├── /
│   ├── +
│   │   └── A
│   │   └── B
│   ├── +
│   │   ├── +
│   │   │   └── C
│   │   │   └── CC
│   │   └── CCC
```
