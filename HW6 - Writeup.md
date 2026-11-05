The Lempel-Ziv algorithm is supposed to compress a string to store it in a more memory-efficient way.
The general idea of the algorithm is to utilize a dictionary/hash map to encode the longest "words" found
in the text, there by reducing redundancies and required space. This implementation of the 
Lempel-Ziv algorithm took five strings and compressed them, the results of which are shown below:  
1. ABBABBABABABABBA : 0 1 1 10 100 110 10 101 0
2. ABCAACBCAA : 0 1 10 0 0 10 100 110
3. QWERTYUIOP : 0 1 10 11 100 101 110 111 1000 1001
4. TREESARECOOL : 0 1 10 10 11 100 1001 101 110 110 111
5. FRIARFDIEFRRII : 0 1 10 11 1 0 100 10 101 110 111 10

Ignoring the spaces, which are purely outputted for readability, all five strings were compressed and saved a 
significant amount of space without compromising any data. For the first string, ABBABBABABABABBA, the original 
string contained 16 characters, bringing the total size to 128 bits. However, the Lempel-Ziv algorithm compresses 
this string down to just 17 bits, an 88% reduction. The second string yields similar results, as the 80-bit string
is reduced by 83% to just 14 bits. An interesting case can be seen in the third string, which is also 80 bits, but 
is only by 68% to 26 bits. This is because the Lempel-Ziv algorithm draws much of its compressing power from repetitions
and redundancies in strings. Since every single character is unique in the third string, there are no repetitions 
or redundancies, which greatly reduces the effectiveness of the Lempel-Ziv algorithm. A similar, but less severe, 
case of this can also be seen in the fourth string, where the Lempel-Ziv algorithm can only compress the 96-bit string
by 72% to 27 bits due to the limited redundancies and repetitions. The fifth string bridges the gap between the 
previous four strings, as it has a moderate amount of unique characters, but also a good amount of redundancies and 
repetitions such that the algorithm can reduce the 112-bit string by 79% down to 24 bits. 