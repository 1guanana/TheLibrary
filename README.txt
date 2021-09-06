       .--.                   .---.
   .---|__|           .-.     |~~~|
.--|===|--|_          |_|     |~~~|--.
|  |===|  |'\     .---!~|  .--|   |--|
|%%|   |  |.'\    |===| |--|%%|   |  |
|%%|   |  |\.'\   |   | |__|  |   |  |
|  |   |  | \  \  |===| |==|  |   |  |
|  |   |__|  \.'\ |   |_|__|  |~~~|__|
|  |===|--|   \.'\|===|~|--|%%|~~~|--|
^--^---'--^    `-'`---^-^--^--^---'--'

Welcome to the Library! This app, meant for use by hobbyist librarians, consists of a SQLite database of classic and new literature and an easy-to-use GUI written in Java that allows users to search, add, delete, and check books in and out.

Java is required to run the .jar executable.

If the database does not yet exist, it will be built and populated upon first running the application. Books can then be searched by thier ISBN number, title, author, or genre. Or, if the search field is left blank and the submit button pressed, the user can browse through all of the books in the database. Users can also delete books (providing either the ISBN or title), add books (providing at least the ISBN, title, and author), and check books in and out (providing the ISBN).

NOTE: If two books have the same name and the user is deleting by title, both books will be deleted from the database! In this case of duplicates, delete using the ISBN. Also, if no year is entered when adding a book, the default year is 0.

Included is both a compiled executable (.jar) of the application and the source code to be reviewed. Enjoy your stay at the Library!
