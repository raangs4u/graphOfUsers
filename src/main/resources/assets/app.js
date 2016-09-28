
var CreateUserForm = React.createClass({
                                        propTypes: {
                                            value: React.PropTypes.object.isRequired,
                                            onChange: React.PropTypes.func.isRequired,
                                            onSubmit: React.PropTypes.func.isRequired
                                        },

                                        onNameChange: function(e) {
                                            this.props.onChange(Object.assign({}, this.props.value, {name: e.target.value}));
                                        },

                                        onAgeChange: function(e) {
                                            this.props.onChange(Object.assign({}, this.props.value, {age: e.target.value}));
                                        },

                                       onSexChange: function(e) {
                                           this.props.onChange(Object.assign({}, this.props.value, {sex: e.target.value}));
                                       },

                                        onLocationChange: function(e) {
                                            this.props.onChange(Object.assign({}, this.props.value, {location: e.target.value}));
                                        },

                                        onSubmit: function(e) {
                                            e.preventDefault();
                                            this.props.onSubmit();
                                        },

                                        render: function() {
                                            return (
                                                <form action="" onSubmit={this.onSubmit}>
                                                    <h2>Create User</h2>
                                                    <div className="form-group">
                                                        <label htmlFor="name">Name *</label>
                                                        <input className="form-control" name="name" ref="name" required value={this.props.value.name} onChange={this.onNameChange} type="text"/>
                                                    </div>
                                                    <div className="form-group">
                                                        <label htmlFor="age">Age *</label>
                                                        <input className="form-control" name="age" ref="age" required value={this.props.value.age} onChange={this.onAgeChange} type="number"/>
                                                    </div>
                                                    <div className="form-group">
                                                        <label htmlFor="sex">Sex *</label>
                                                        <select className="form-control" name="sex" ref="sex" required value={this.props.value.sex} onChange={this.onSexChange}>
                                                            <option value="male">Male</option>
                                                            <option value="female">Female</option>
                                                        </select>
                                                    </div>
                                                    <div className="form-group">
                                                        <label htmlFor="location">Location *</label>
                                                        <input className="form-control" name="location" ref="location" required value={this.props.value.location} onChange={this.onLocationChange} type="text"/>
                                                    </div>
                                                    <div className="form-group">
                                                        <button className="btn btn-primary" type="submit">Add User</button>
                                                    </div>
                                                </form>
                                            );
                                        }
                                    });

var CreateRelationForm = React.createClass({
                                           propTypes: {
                                               value: React.PropTypes.object.isRequired,
                                               users: React.PropTypes.array.isRequired,
                                               onChange: React.PropTypes.func.isRequired,
                                               onSubmit: React.PropTypes.func.isRequired
                                           },

                                           onUser1Change: function(e) {
                                               var user1 = this.props.users[e.target.value];
                                               this.props.onChange(Object.assign({}, this.props.value, {user1}));
                                           },

                                           onUser2Change: function(e) {
                                               var user2 = this.props.users[e.target.value];
                                               this.props.onChange(Object.assign({}, this.props.value, {user2}));
                                           },
                                           onRelationChange: function(e) {
                                               this.props.onChange(Object.assign({}, this.props.value, {relationType: e.target.value}));
                                           },
                                           onSubmit: function(e) {
                                               e.preventDefault();
                                               var user1 = this.props.value.user1.id;
                                               var user2 = this.props.value.user2.id;
                                               if (user1 == user2) {
                                                   alert("Please select different users.");
                                                   return;
                                               }

                                               this.props.onSubmit();
                                           },

                                           render: function() {
                                               var userOptionElements = [];
                                               for (var i=0; i<this.props.users.length;i++) {
                                                   var o = this.props.users[i];
                                                   userOptionElements.push(<option key={i} value={i}>{o.name}</option>)
                                               }
                                               return (
                                                   <form action="" onSubmit={this.onSubmit}>
                                                       <h2>Create Relationship</h2>
                                                       <div className="form-group">
                                                           <label htmlFor="user1">User1 *</label>
                                                           <select className="form-control" name="user1" ref="user1" required onChange={this.onUser1Change}>
                                                               {userOptionElements}
                                                           </select>
                                                       </div>
                                                       <div className="form-group">
                                                           <label htmlFor="user2">User2 *</label>
                                                           <select className="form-control" name="user2" ref="user2" required onChange={this.onUser2Change}>
                                                               {userOptionElements}
                                                           </select>
                                                       </div>
                                                       <div className="form-group">
                                                           <label htmlFor="relationType">Relation type *</label>
                                                           <select className="form-control" name="relationType" ref="relationType" required onChange={this.onRelationChange}>
                                                               <option value="KNOWS">Knows</option>
                                                               <option value="FRIEND">Friend</option>
                                                               <option value="RELATIVE">Relative</option>
                                                           </select>
                                                       </div>
                                                       <div className="form-group">
                                                           <button className="btn btn-primary" type="submit">Add Relationship</button>
                                                       </div>
                                                   </form>
                                               );
                                           }
                                           });

var CreateShortestPathForm = React.createClass({
                                               propTypes: {
                                                   value: React.PropTypes.object.isRequired,
                                                   users: React.PropTypes.array.isRequired,
                                                   onChange: React.PropTypes.func.isRequired,
                                                   onSubmit: React.PropTypes.func.isRequired
                                               },

                                               onUser1Change: function(e) {
                                                   var user1 = this.props.users[e.target.value];
                                                   this.props.onChange(Object.assign({}, this.props.value, {user1}));
                                               },

                                               onUser2Change: function(e) {
                                                   var user2 = this.props.users[e.target.value];
                                                   this.props.onChange(Object.assign({}, this.props.value, {user2}));
                                               },
                                               onSubmit: function(e) {
                                                   e.preventDefault();
                                                   var user1 = this.props.value.user1.id;
                                                   var user2 = this.props.value.user2.id;
                                                   debugger;
                                                   if (user1 == user2) {
                                                       alert("Please select different users.");
                                                       return;
                                                   }

                                                   this.props.onSubmit();
                                               },

                                               render: function() {
                                                   var userOptionElements = [];
                                                   for (var i=0; i<this.props.users.length;i++) {
                                                       var o = this.props.users[i];
                                                       userOptionElements.push(<option key={i} value={i}>{o.name}</option>)
                                                   }
                                                   return (
                                                       <form action="" onSubmit={this.onSubmit}>
                                                           <h2>Shortest path for</h2>
                                                           <div className="form-group">
                                                               <label htmlFor="user1">User1 *</label>
                                                               <select className="form-control" name="user1" ref="user1" required onChange={this.onUser1Change}>
                                                                   {userOptionElements}
                                                               </select>
                                                           </div>
                                                           <div className="form-group">
                                                               <label htmlFor="user2">User2 *</label>
                                                               <select className="form-control" name="user2" ref="user2" required onChange={this.onUser2Change}>
                                                                   {userOptionElements}
                                                               </select>
                                                           </div>

                                                           <div className="form-group">
                                                               <button className="btn btn-primary" type="submit">Get Shortest path</button>
                                                           </div>
                                                       </form>
                                                   );
                                               }
                                           });

var ShortestPathDisplay = React.createClass({
                                                propTypes: {
                                                    users: React.PropTypes.array.isRequired
                                                },

                                                render: function() {
                                                    var path = "";
                                                    var users = this.props.users;
                                                    for (var i=0; i<users.length-1;i++) {
                                                        path += users[i].name + ' --> '
                                                    }
                                                    if (users.length >=2 ) {
                                                        path += users[users.length - 1].name;
                                                    } else {
                                                        path = "No path exists."
                                                    }
                                                    return (
                                                        <div className = "ShortestpathItem">
                                                            <h2 className = "shortestpath-name">Shortest path</h2>
                                                            <p className="shortestpath-view">{path}</p>
                                                        </div>
                                                    );
                                                }
                                            });



var UserItem = React.createClass({
                                        propTypes: {
                                            name: React.PropTypes.string.isRequired,
                                            age: React.PropTypes.number.isRequired,
                                            sex: React.PropTypes.string.isRequired,
                                            location: React.PropTypes.string.isRequired
                                        },

                                        render: function() {
                                            return (
                                                <tr>
                                                    <td>{this.props.name}</td>
                                                    <td>{this.props.age}</td>
                                                    <td>{this.props.sex}</td>
                                                    <td>{this.props.location}</td>
                                                </tr>
                                            );
                                        }
                                    });


var UserView = React.createClass({
                                        propTypes: {
                                            users: React.PropTypes.array.isRequired,
                                            newUser: React.PropTypes.object.isRequired,
                                            onNewUserChange: React.PropTypes.func.isRequired,
                                            onNewUserSubmit: React.PropTypes.func.isRequired
                                        },

                                        render: function() {
                                            var userItemElements = this.props.users
                                                .map(function(user) { return getTableRow(user) });
                                            return (
                                                <div>
                                                    <h1>Users</h1>

                                                    <table className="table table-hover">
                                                        <thead>
                                                            <tr>
                                                                <th>Name</th>
                                                                <th>Age</th>
                                                                <th>Sex</th>
                                                                <th>Location</th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>{userItemElements}</tbody>
                                                    </table>
                                                    <CreateUserForm value={this.props.newUser} onChange={this.props.onNewUserChange} onSubmit={this.props.onNewUserSubmit}></CreateUserForm>
                                                </div>
                                            );
                                        }
                                    });

var RelationView = React.createClass({
                                     propTypes: {
                                         users: React.PropTypes.array.isRequired,
                                         newRelation: React.PropTypes.object.isRequired,
                                         newPath: React.PropTypes.object.isRequired,
                                         onNewRelationChange: React.PropTypes.func.isRequired,
                                         onNewRelationSubmit: React.PropTypes.func.isRequired,
                                         onNewPathChange: React.PropTypes.func.isRequired,
                                         onNewPathSubmit: React.PropTypes.func.isRequired
                                     },

                                     render: function() {

                                         return (
                                             <div className="RelationView">
                                                 <CreateRelationForm value={this.props.newRelation} users={this.props.users} onChange={this.props.onNewRelationChange} onSubmit={this.props.onNewRelationSubmit}></CreateRelationForm>
                                                 <CreateShortestPathForm value={this.props.newPath} users={this.props.users} onChange={this.props.onNewPathChange} onSubmit={this.props.onNewPathSubmit}></CreateShortestPathForm>
                                             </div>

                                         );
                                     }
                                 });



function getTableRow(user) {
    return (
        <tr>
            <td>{user.name}</td>
            <td>{user.age}</td>
            <td>{user.sex}</td>
            <td>{user.location}</td>
        </tr>
    )
}
/*
 * Constants
 */


var USER_TEMPLATE = {name: "", age: "", sex: "male", location: ""};
var RELATION_TEMPLATE = {user1: undefined, user2: undefined, relationType: "KNOWS"};



/*
 * Actions
 */


function updateNewUser(user) {
    setUserState({ newUser: user });
}

function updateNewRelation(relation) {
    setRelationState({ newRelation: relation });
}

function updateNewPath(path) {
    setRelationState({ newPath: path });
}

function submitNewUser() {
    var user = Object.assign({}, userState.newUser, {id: userState.users.length + 1});
    //alert(user.name);

    if (user.name) {
        var xhr = new XMLHttpRequest();
        xhr.open("POST", '/user', true);
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4 && xhr.status == 200) {
                var users = JSON.parse(xhr.responseText);
                var tempUser = users.length > 0 ? users[0] : undefined;
                setUserState(
                    {
                        newUser: Object.assign({}, USER_TEMPLATE),
                        users: users
                    }
                );
                setRelationState({newRelation: Object.assign({}, RELATION_TEMPLATE, {user1:tempUser, user2: tempUser}), users: users});
                alert("User successfully created");
            }
        };
        xhr.setRequestHeader('Content-type', 'application/json');
        xhr.send(JSON.stringify(user));
    }

}

function submitNewRelation() {
    var relation = Object.assign({}, relationState.newRelation, {id: userState.users.length + 1});

    var xhr = new XMLHttpRequest();
    xhr.open("POST", '/relation', true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4 && xhr.status == 200) {
            alert("Relationship successfully created");
        }
    };
    xhr.setRequestHeader('Content-type', 'application/json');
    xhr.send(JSON.stringify(relation));

}

function getShortestPath() {
    var path = Object.assign({}, relationState.newPath);
    var xhr = new XMLHttpRequest();
    xhr.open("GET", '/shortest-path/'+path.user1.id+'/'+path.user2.id, true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4 && xhr.status == 200) {
            var users = JSON.parse(xhr.responseText);
            ReactDOM.render(
                React.createElement(ShortestPathDisplay, Object.assign({}, {
                    users: users
                })),
                document.getElementById('shortestpath-content')
            );
        }
    };
    xhr.send(null);
}



// The user's complete current state
var userState = {};
var relationState = {};


function setUserState(changes) {
    Object.assign(userState, changes);

    ReactDOM.render(
        React.createElement(UserView, Object.assign({}, userState, {
            onNewUserChange: updateNewUser,
            onNewUserSubmit: submitNewUser
        })),
        document.getElementById('user-content')
    );
}

function setRelationState(changes) {
    Object.assign(relationState, changes);

    ReactDOM.render(
        React.createElement(RelationView, Object.assign({}, relationState, {
            onNewRelationChange: updateNewRelation,
            onNewRelationSubmit: submitNewRelation,
            onNewPathChange: updateNewPath,
            onNewPathSubmit: getShortestPath
        })),
        document.getElementById('relation-content')
    );
}


var xhr = new XMLHttpRequest();
xhr.open("GET", '/users', true);
xhr.onreadystatechange = function () {
    if (xhr.readyState == 4 && xhr.status == 200) {
        var users = JSON.parse(xhr.responseText);
        var tempUser = users.length > 0 ? users[0] : undefined;
        setUserState({users: users, newUser: Object.assign({}, USER_TEMPLATE)});
        setRelationState({newRelation: Object.assign({}, RELATION_TEMPLATE, {user1:tempUser, user2: tempUser}), users: users, newPath:{user1:tempUser, user2: tempUser}});
    }
};
xhr.send(null);



