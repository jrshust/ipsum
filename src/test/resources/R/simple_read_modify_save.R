data <- read.csv("/path/to/some/data")

another_var <- fancy_function(data, "literal", 22, FALSE)
not_related <- data
derived <- another_var$sub_thing

ggplot(derived, aes(x = Field1.Nice, y = Field2.Nice, color = Field3)) + geom_point()

