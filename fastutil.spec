%define section free

Name:           fastutil
Version:        4.3.1
Release:        0jpp
Epoch:          0
Summary:        Fast & compact type-specific Java utility classes
License:        LGPL
Source0:        http://fastutil.dsi.unimi.it/fastutil-4.3.1-src.tar.gz
URL:            http://fastutil.dsi.unimi.it/
Group:          Development/Libraries/Java
Vendor:         JPackage Project
Distribution:   JPackage
BuildArch:      noarch
BuildRequires:  ant, make, gcc, jpackage-utils >= 0:1.5, /bin/bash
BuildRequires:	java-javadoc
BuildRoot:      %{_tmppath}/%{name}-%{version}-%{release}-buildroot

%description
fastutil extends the Java Collections Framework by providing type-specific
maps, sets, lists and priority queues with a small memory footprint and
fast access and insertion. The classes implement their standard counterpart
interface (e.g., Map for maps) and can be plugged into existing code.
Moreover, they provide additional features (such as bidirectional
iterators) that are not available in the standard classes.


%package        javadoc
Summary:        Javadoc for %{name}
Group:          Development/Documentation

%description    javadoc
Javadoc for %{name}.

%prep
%setup -q

%build
make sources
export CLASSPATH=
ant \
  -Dj2se.apiurl=%{_javadocdir}/java \
  jar javadoc


%install
rm -rf $RPM_BUILD_ROOT
# jars
install -dm 755 $RPM_BUILD_ROOT%{_javadir}
install -pm 644 %{name}-%{version}.jar $RPM_BUILD_ROOT%{_javadir}
ln -s %{name}-%{version}.jar $RPM_BUILD_ROOT%{_javadir}/%{name}.jar
# javadoc
install -dm 755 $RPM_BUILD_ROOT%{_javadocdir}/%{name}-%{version}
cp -pr docs/* $RPM_BUILD_ROOT%{_javadocdir}/%{name}-%{version}
ln -s %{name}-%{version} $RPM_BUILD_ROOT%{_javadocdir}/%{name} # ghost symlink


%clean
rm -rf $RPM_BUILD_ROOT


%post javadoc
rm -f %{_javadocdir}/%{name}
ln -s %{name}-%{version} %{_javadocdir}/%{name}
 

%files
%defattr(0644,root,root,0755)
%doc README CHANGES COPYING.LIB
%{_javadir}/*.jar

%files javadoc
%defattr(0644,root,root,0755)
%{_javadocdir}/%{name}-%{version}
%ghost %doc %{_javadocdir}/%{name}

# -----------------------------------------------------------------------------

%changelog
* Tue Jun 29 2004 Sebastiano Vigna <vigna at acm.org> - 4.3-1jpp
- Fixes to trim() methods, and new unmodifiable structures.

* Tue May 04 2004 Sebastiano Vigna <vigna at acm.org> - 4.2-1jpp
- Fixes to array classes.

* Mon Feb 09 2004 Sebastiano Vigna <vigna at acm.org> - 4.1-1jpp
- New custom hash classes.

* Sun Jan 18 2004 Sebastiano Vigna <vigna at acm.org> - 4.0-1jpp
- Several implementations of priority queues.

* Fri Nov 01 2003 Sebastiano Vigna <vigna at acm.org> - 3.1-1jpp
- Update to 3.1: Several new static containers.

* Wed Jul 04 2003 Sebastiano Vigna <vigna at acm.org> - 3.0.1-1jpp
- A small but important serialisation bug fix.

* Thu Jun 26 2003 Sebastiano Vigna <vigna at acm.org> - 3.0-1jpp
- First release of version 3.0.

* Sun Apr 06 2003 Sebastiano Vigna <vigna at acm.org> - 2.60-1jpp
- Improved iterators.

* Fri Apr  4 2003 Ville Skyttä <ville.skytta at iki.fi> - 0:2.52-2jpp
- Rebuilt for JPackage 1.5.

* Thu Mar 13 2003 Ville Skyttä <ville.skytta at iki.fi> - 2.52-1jpp
- Updated to 2.52.
- Comments from upstream (thanks to Sebastiano Vigna):
  - IMPORTANT: The package name has changed. Please remove manually
    the old package (if you have a RPM called "fastUtil" installed)
    and modify your sources.
  - Deleted docs from source tar.
  - Merged in patches to Makefile.

* Sun Mar  9 2003 Ville Skyttä <ville.skytta at iki.fi> - 2.51-2jpp
- First official JPackage release.
- Fix Group tags.
- No manual subpackage (there's no manual... :)

* Wed Feb 26 2003 Sebastiano Vigna <vigna at acm.org> 2.51
- New flexible trim(int) method..

* Tue Feb 18 2003 Sebastiano Vigna <vigna at acm.org> 2.50
- New linked hash tables and reference-based containers.

* Mon Dec 9 2002 Sebastiano Vigna <vigna at acm.org> 2.11
- JPackage compatible version
