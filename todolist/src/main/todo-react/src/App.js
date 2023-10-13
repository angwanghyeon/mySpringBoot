import './App.css';
import {useEffect, useState} from "react";
import {Container, List, Paper} from "@mui/material";
import AddTodo from "./AddTodo";
import Todo from "./Todo";
import {call} from "./ApiService";


function App() {
    const [items, setItems] = useState([]);

    useEffect(() => {
        call("/todo", "GET", null)
            .then((response) => setItems(response.data));
    }, []);

    const addItem = (item) => {
            //call function을 사용하여 더 쉽게 해보자
            // item.id = "ID-"+ items.length;
            // item.done = false;
            // setItems([...items, item]);
            // console.log("========= items : "+ items);
        call("/todo","POST", item)
            .then((response) => setItems(response.data));
    }

    const editItem = (item) => {
        //update도 call을 사용해보자
        // setItems([...items]);
        call("/todo", "PUT", item)
            .then((response) => setItems(response.data));
    }

    const deleteItem = (item) => {
        call("/todo", "DELETE", item)
            .then((response) => setItems(response.data));
        //delete도 call 써서 지우기
        // const newItems = items.filter(e => e.id !== item.id);
        // setItems([...newItems]);
    }

    let todoItems = items && items.length > 0 && (
        <Paper style={{margin:16}}>
            <List>
                {items.map((item) => (
                    <Todo item={item} key={item.id}
                          editItem={editItem} deleteItem={deleteItem}>
                    </Todo>
                ))}
            </List>
        </Paper>
    )
  return (
    <div className="App">
        <Container maxWidth="md">
           <AddTodo addItem={addItem}></AddTodo>
           <div className="TodoList">
               {todoItems}
           </div>
        </Container>
    </div>
  );
}

export default App;
