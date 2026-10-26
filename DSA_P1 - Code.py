# LeetCode Problem 686
def KMP(pat: str, txt: str) -> bool:
    lps = [0] * len(pat)
    i = 1
    siz = 0
    while i < len(pat):
        if pat[i] == pat[siz]:
            siz = siz + 1
            lps[i] = siz
            i = i + 1
        elif siz == 0:
            lps[i] = 0
            i = i + 1
        else:
            siz = lps[siz - 1]
    i = 0
    j = 0
    while i < len(txt):
        if txt[i] == pat[j]:
            i = i + 1
            j = j + 1
            if j == len(pat):
                return True
        else:
            if j != 0:
                j = lps[j - 1]
            else:
                i = i + 1
    return False
def repeatedStringMatch(a: str, b: str) -> int:
    aLong = a
    count = 1
    while len(aLong) < len(b):
        aLong = aLong + a
        count = count + 1
    if KMP(b, aLong):
        return count
    if KMP(b, aLong + a):
        return count + 1
    return -1

# LeetCode Problem 459
def repeatedSubstringPattern(s: str) -> bool:
    lps = [0] * len(s)
    i = 1
    siz = 0
    while i < len(s):
        if s[i] == s[siz]:
            siz = siz + 1
            lps[i] = siz
            i = i + 1
        elif siz == 0:
            lps[i] = 0
            i = i + 1
        else:
            siz = lps[siz - 1]
    if lps[-1] == 0:
        return False
    return s == s[0:-lps[-1]] * int((len(s) / len(s[0:-lps[-1]])))