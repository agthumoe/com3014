<div class="row well">
    <div class="col-lg-10 col-md-10">
        <div class="col-lg-3 col-md-3">
            <div class="form-group">
                <label for="filter_by">Filter By: </label>
                <select id="filter_by" class="form-control input-sm">
                    <option value="username">Username</option>
                    <option value="email">Email</option>
                    <option value="name">Name</option>
                    <option value="enabled">Status</option>
                    <option value="role">Role</option>
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
                <label for="filter_limit">Limit: </label>
                <input name="data[User][limit]" id="filter_limit" class="form-control input-sm" pattern="[0-9]{1,2}"
                       title="Must be a number < 100" type="text" value="10">
            </div>
        </div>
    </div>
    <div class="col-lg-2 col-md-2">
        <div class="pull-right">
            <br>
            <a href="#" class="btn btn-default btn-sm" id="btn-filter"><i class="fa fa-filter"></i></a>
            <a href="#" class="btn btn-primary btn-sm" id="btn-reset-filter"><span class="fa fa-refresh"></span></a>
            <a href="/admin/users/new" role="button" class="btn btn-success btn-sm"><span class="fa fa-plus"></span></a>
        </div>
    </div>
</div>
