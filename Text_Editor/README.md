

This is a simple Text Editor/ Notes Springboot application.

Developers can add descriptions of Bugs and search by Keywords and Categories, export it as a pdf, etc.




----

Features:

- Notes can be created, edited, deleted, exported to PDF

- Notes persisted to the DB, in particular to a SQLite db specified in the corresponding profile. Switching to another RDBMS is easy, Postgres example is included in the properties

- Notes are listed with a short preview on the home page

- Notes can be assigned to a category. Category name can be chosen via auto-completion based on the existing notes' categories

- Search function is performed based on the note content and/or category

- Notes can be edited by selecting them on the View and choosing Edit

- Notes can be deleted by choosing to edit them and then clicking the Delete button. User will be prompted with a Confirmation dialog to complete this action.


Screenshots are provided on the main source folder


