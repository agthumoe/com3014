<div class="row well">
    <div class="col-lg-10 col-md-10">
        <div class="col-lg-2 col-md-2">
            <div class="form-group">
                <label for="filter_by">Filter By: </label>
                <select id="filter_by" class="form-control input-sm">
                    <option>Username</option>
                    <option>Email</option>
                    <option>Name</option>
                    <option>Created</option>
                    <option>Last Modified</option>
                </select>
            </div>
        </div>
        <div class="col-lg-4 col-md-4">
            <div class="form-group">
                <label for="filter_type">Filter: </label>
                <input id="filter_type" class="form-control input-sm" type="text">
            </div>
        </div>
        <div class="col-lg-2 col-md-2">
            <div class="form-group">
                <label for="type_role">Role: </label>
                <select id="type_role" class="form-control input-sm">
                    <option value="any">Any</option>
                    <option value="Admin">Admin</option>
                    <option value="User">User</option>
                </select>
            </div>
        </div>
        <div class="col-lg-2 col-md-2">
            <div class="form-group">
                <label for="status">Status: </label>
                <select id="status" class="form-control input-sm">
                    <option value="0">Any</option>
                    <option value="1">Active</option>
                    <option value="2">Inactive</option>
                </select>
            </div>
        </div>
        <div class="col-lg-1 col-md-2">
            <div class="form-group">
                <label for="limit">Limit: </label>
                <input name="data[User][limit]" id="limit" class="form-control input-sm" pattern="[0-9]{1,2}"
                       title="Must be a number < 100" type="text" value="10">
            </div>
        </div>
    </div>
    <div class="col-lg-2 col-md-2">
        <div class="pull-right">
            <br>
            <a href="#" class="btn btn-default btn-sm"><i class="fa fa-filter"></i></a>
            <a href="#" class="btn btn-primary btn-sm"><span class="fa fa-refresh"></span></a>
            <a href="/admin/users/new" role="button" class="btn btn-success btn-sm"><span class="fa fa-plus"></span></a>
        </div>
    </div>
</div>
