This project deals with the functioning of a Library Management System titled RentRead. With this, the user can register as a USER or ADMIN and accordingly carry out various responsibilities for the application. This has been done with the help of Basic AUTH.

If the person does not specify which role they need, they will be registered as a USER. A USER can rent upto 2 books only. An ADMIN can create, update and delete books, check which books have been rented out by a user, which users have read a particular book, etc. A person without any role can search for books as per genre, author and title.

Data is persisted with the use of MySQL. I have also generated a JAR file which you will find in the build/libs folder. To run the file, execute the instruction java -jar build/libs/Rent_Read_System-0.0.1-SNAPSHOT.jar

Endpoints:
/register - To register
/admin/user/{email} - An ADMIN can check which books a user has borrowed till date
/admin/book/{title} - An ADMIN can check which users have read a particular book till date
/books - POST - An ADMIN can create books
/books/{title} - PUT - An ADMIN can update books
/books/{title} - DELETE - An ADMIN can delete books
/books/{title} - GET - Anybody can view a particular book
/books - GET ALL - View all books
/books/genre/{genre} - GET by genre
/books/author/{author} - GET by author
/books/{title}/rent - USER can rent the book
/books/{title}/return - USER can return the book