/*
 * Components
 */


var CreateUserForm = React.createClass({
                                        propTypes: {
                                            value: React.PropTypes.object.isRequired,
                                            onChange: React.PropTypes.func.isRequired,
                                            onSubmit: React.PropTypes.func.isRequired,
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
                                                React.createElement('form', {onSubmit: this.onSubmit, className: 'UserForm', noValidate: true},
                                                                    React.createElement('input', {
                                                                        type: 'text',
                                                                        placeholder: 'Name (required)',
                                                                        value: this.props.value.name,
                                                                        onChange: this.onNameChange,
                                                                    }),
                                                                    React.createElement('input', {
                                                                        type: 'Age',
                                                                        placeholder: 'Age (required)',
                                                                        value: this.props.value.age,
                                                                        onChange: this.onAgeChange,
                                                                    }),
                                                                    <select value={this.props.value.sex}>
                                                                        <option value="male">Male</option>
                                                                        <option value="female">Female</option>
                                                                    </select>,
                                                                    React.createElement('input', {
                                                                        placeholder: 'Location (required)',
                                                                        value: this.props.value.location,
                                                                        onChange: this.onLocationChange,
                                                                    }),
                                                                    React.createElement('button', {type: 'submit'}, "Add User")
                                                )
                                            );
                                        },
                                    });


var ContactItem = React.createClass({
                                        propTypes: {
                                            name: React.PropTypes.string.isRequired,
                                            email: React.PropTypes.string.isRequired,
                                            description: React.PropTypes.string,
                                        },

                                        render: function() {
                                            return (
                                                React.createElement('li', {className: 'ContactItem'},
                                                                    React.createElement('h2', {className: 'ContactItem-name'}, this.props.name),
                                                                    React.createElement('a', {className: 'ContactItem-email', href: 'mailto:'+this.props.email}, this.props.email),
                                                                    React.createElement('div', {className: 'ContactItem-description'}, this.props.description)
                                                )
                                            );
                                        },
                                    });


var ContactView = React.createClass({
                                        propTypes: {
                                            contacts: React.PropTypes.array.isRequired,
                                            newContact: React.PropTypes.object.isRequired,
                                            onNewContactChange: React.PropTypes.func.isRequired,
                                            onNewContactSubmit: React.PropTypes.func.isRequired,
                                        },

                                        render: function() {
                                            var contactItemElements = this.props.contacts
                                                .filter(function(contact) { return contact.email; })
                                                .map(function(contact) { return React.createElement(ContactItem, contact); });

                                            return (
                                                React.createElement('div', {className: 'ContactView'},
                                                                    React.createElement('h1', {className: 'ContactView-title'}, "Users"),
                                                                    React.createElement('ul', {className: 'ContactView-list'}, contactItemElements),
                                                                    React.createElement(CreateUserForm, {
                                                                        value: this.props.newContact,
                                                                        onChange: this.props.onNewContactChange,
                                                                        onSubmit: this.props.onNewContactSubmit,
                                                                    })
                                                )
                                            );
                                        },
                                    });


/*
 * Constants
 */


var CONTACT_TEMPLATE = {name: "", email: "", description: "", errors: null};



/*
 * Actions
 */


function updateNewContact(contact) {
    setState({ newContact: contact });
}


function submitNewContact() {
    var contact = Object.assign({}, state.newContact, {key: state.contacts.length + 1, errors: {}});

    if (contact.name && contact.email) {
        setState(
            Object.keys(contact.errors).length === 0
                ? {
                newContact: Object.assign({}, CONTACT_TEMPLATE),
                contacts: state.contacts.slice(0).concat(contact),
            }
                : { newContact: contact }
        );
    }
}


/*
 * Model
 */


// The app's complete current state
var state = {};

// Make the given changes to the state and perform any required housekeeping
function setState(changes) {
    Object.assign(state, changes);

    ReactDOM.render(
        React.createElement(ContactView, Object.assign({}, state, {
            onNewContactChange: updateNewContact,
            onNewContactSubmit: submitNewContact,
        })),
        document.getElementById('react-app')
    );
}

// Set initial data
setState({
             contacts: [
                 {key: 1, name: "James K Nelson", email: "james@jamesknelson.com", description: "Front-end Unicorn"},
                 {key: 2, name: "Jim", email: "jim@example.com"},
             ],
             newContact: Object.assign({}, CONTACT_TEMPLATE),
         });
