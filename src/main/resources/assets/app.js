var CreateUserForm = React.createClass({
                                           propTypes: {
                                               //value: React.PropTypes.object.isRequired,
                                               //onChange: React.PropTypes.func.isRequired,
                                               //onSubmit: React.PropTypes.func.isRequired,
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
                                                                           onChange: this.onNameChange
                                                                       }),
                                                                       React.createElement('input', {
                                                                           type: 'Age',
                                                                           placeholder: 'Age (required)',
                                                                           value: this.props.value.age,
                                                                           onChange: this.onAgeChange
                                                                       }),
                                                       <select value={this.props.value.sex}>
                                                           <option value="male">Male</option>
                                                           <option value="female">Female</option>
                                                       </select>,
                                                                       React.createElement('input', {
                                                                           placeholder: 'Location (required)',
                                                                           value: this.props.value.location,
                                                                           onChange: this.onLocationChange
                                                                       }),
                                                                       React.createElement('button', {type: 'submit'}, "Add User")
                                                   )
                                               );
                                           },
                                       });
ReactDOM.render(
<CreateUserForm />,
    document.getElementById('content')
);